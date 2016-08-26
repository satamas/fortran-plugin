package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.lang.FortranNodeType;

import static org.jetbrains.fortran.lang.FortranNodeTypes.*;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.*;

public class FortranExpressionParsing extends AbstractFortranParsing {
    static final public TokenSet TYPE_FIRST = TokenSet.create(
            REAL_KEYWORD, INTEGER_KEYWORD, TYPE_KEYWORD, LOGICAL_KEYWORD, CHARACTER_KEYWORD, COMPLEX_KEYWORD);

    static final public TokenSet EXPRESSION_FIRST = TokenSet.create(
            LPAR,
            OPENING_QUOTE,
            INTEGER_LITERAL,
            FLOATING_POINT_LITERAL,
            TRUE_KEYWORD,
            FALSE_KEYWORD,
            IDENTIFIER
    );

    public enum Precedence {
        PREFIX(MINUS, PLUS) { // annotations
            @Override
            public void parseHigherPrecedence(FortranExpressionParsing parser) {
                throw new IllegalStateException("Don't call this method");
            }
        },

        EXPONENTIATION(POWER) {
            @Override
            public void parseHigherPrecedence(FortranExpressionParsing parser) {
                parser.parsePrefixExpression();
            }
        },
        MULTIPLICATIVE(MUL, DIV),
        ADDITIVE(PLUS, MINUS),
        CONCAT(DIVDIV),
        RELATIONAL(EQEQ, NEQ, LT, LE, GT, GE),
        NEGATION(NOT),
        CONJUNCTION(AND) {
            @Override
            public void parseHigherPrecedence(FortranExpressionParsing parser) {
                parser.parseAndOperand();
            }
        },
        DISJUNCTION(OR),
        EQUIVALENCE(LOGICAL_EQ, LOGICAL_NEQ),
        ASSIGNMENT(EQ) {
            @Override
            public FortranNodeType parseRightHandSide(IElementType operation, FortranExpressionParsing parser) {
                if(parser.at(MUL)) {
                    parser.advance();
                    return WILDCARD;
                } else {
                    return super.parseRightHandSide(operation, parser);
                }
            }
        };

        static {
            Precedence[] values = Precedence.values();
            for (Precedence precedence : values) {
                int ordinal = precedence.ordinal();
                precedence.higher = ordinal > 0 ? values[ordinal - 1] : null;
            }
        }

        private Precedence higher;
        private final TokenSet operations;

        Precedence(IElementType... operations) {
            this.operations = TokenSet.create(operations);
        }

        public void parseHigherPrecedence(FortranExpressionParsing parser) {
            assert higher != null;
            parser.parseBinaryExpression(higher);
        }

        /**
         * @param operation the operation sign (e.g. PLUS or IS)
         * @param parser    the parser object
         * @return node type of the result
         */
        public FortranNodeType parseRightHandSide(IElementType operation, FortranExpressionParsing parser) {
            parseHigherPrecedence(parser);
            return BINARY_EXPRESSION;
        }

        @NotNull
        public final TokenSet getOperations() {
            return operations;
        }
    }

    public FortranExpressionParsing(WhitespaceAwarePsiBuilder builder) {
        super(builder);
    }


    public void parseExpression() {
        if (!atSet(EXPRESSION_FIRST)) {
            error("Expecting an expression");
            return;
        }
        parseBinaryExpression(Precedence.ASSIGNMENT);
    }

    private void parseAndOperand() {
        if(atSet(Precedence.NEGATION.getOperations())) {
            PsiBuilder.Marker prefixExpression = mark();
            parseOperationReference();
            parseBinaryExpression(Precedence.RELATIONAL);
            prefixExpression.done(PREFIX_EXPRESSION);
        } else {
            parseBinaryExpression(Precedence.RELATIONAL);
        }
    }

    public void parsePrefixExpression() {
        if(atSet(Precedence.PREFIX.getOperations())) {
            PsiBuilder.Marker prefixExpression = mark();
            parseOperationReference();
            parsePostfixExpression();
            prefixExpression.done(PREFIX_EXPRESSION);
        } else {
            parsePostfixExpression();
        }
    }

    public void parsePostfixExpression() {
        PsiBuilder.Marker expression = mark();
        parseAtomicExpression();
        if (parseCallOrAccessSuffix()) {
            expression.done(CALL_OR_ACCESS_EXPRESSION);
        } else {
            expression.drop();
        }
    }

    public boolean parseCallOrAccessSuffix() {
        if (at(LPAR)) {
            parseValueArgumentList();
            return true;
        } else {
            return false;
        }
    }

    public void parseValueArgumentList() {
        PsiBuilder.Marker list = mark();

        if (expect(LPAR, "Expecting an argument list")) {
            if (!at(RPAR)) {
                while (true) {
                    while (at(COMMA)) errorAndAdvance("Expecting an argument");
                    parseAtomicExpression();
                    if (!at(COMMA)) break;
                    advance(); // COMMA
                    if (at(RPAR)) {
                        error("Expecting an argument");
                        break;
                    }
                }
            }

            expect(RPAR, "Expecting ')'");
        }

        list.done(ARGUMENT_LIST);
    }

    private boolean parseAtomicExpression() {
        if (at(LPAR)) {
            parseParenthesizedExpression();
        } else if (at(OPENING_QUOTE)) {
            parseString();
        } else if (at(IDENTIFIER)) {
            parseSimpleNameExpression();
        } else if (!parseLiteralConstant()) {
            error("Expecting an element");
            return false;
        }

        return true;
    }

    private void parseParenthesizedExpression() {
        assert at(LPAR);

        PsiBuilder.Marker mark = mark();

        advance(); // LPAR
        if (at(RPAR)) {
            error("Expecting an expression");
        } else {
            parseExpression();
        }

        expect(RPAR, "Expecting ')'");

        mark.done(PARENTHESIZED);
    }

    public void parseString() {
        PsiBuilder.Marker stringLiteral = mark();
        assert at(OPENING_QUOTE);
        advance();
        while (!eof() && at(REGULAR_STRING_PART)) {
            advance();
        }
        expect(CLOSING_QUOTE, "\" expected");
        stringLiteral.done(STRING_CONSTANT);
    }

    public void parseSimpleNameExpression() {
        PsiBuilder.Marker simpleName = mark();
        expect(IDENTIFIER, "Expecting an identifier");
        simpleName.done(REFERENCE_EXPRESSION);
    }

    private void parseOperationReference() {
        PsiBuilder.Marker operationReference = mark();
        advance(); // operation
        operationReference.done(OPERATION_REFERENCE);
    }

    public void parseBinaryExpression(Precedence precedence) {
        PsiBuilder.Marker expression = mark();

        precedence.parseHigherPrecedence(this);

        while (!at(SEMICOLON) && atSet(precedence.getOperations())) {
            IElementType operation = tt();

            parseOperationReference();

            FortranNodeType resultType = precedence.parseRightHandSide(operation, this);
            expression.done(resultType);
            expression = expression.precede();
        }

        expression.drop();
    }

    private boolean parseLiteralConstant() {
        PsiBuilder.Marker marker = mark();
        if (at(IDENTIFIER)) {
            advance();
            marker.done(REFERENCE_EXPRESSION);
        } else if (at(INTEGER_LITERAL)) {
            advance();
            marker.done(INTEGER_CONSTANT);
        } else if (at(FLOATING_POINT_LITERAL)) {
            advance();
            marker.done(FLOATING_POINT_CONSTANT);
        } else if (at(DOUBLE_PRECISION_LITERAL)) {
            advance();
            marker.done(DOUBLE_PRECISION_CONSTANT);
        } else if (at(LPAR)) {
            parseComplexConstant();
            marker.drop();
        } else if (at(OPENING_QUOTE)) {
            parseString();
            marker.drop();
        } else if (at(TRUE_KEYWORD) || at(FALSE_KEYWORD)) {
            advance();
            marker.done(BOOLEAN_CONSTANT);
        } else {
            return false;
        }
        return true;
    }

    private void parseComplexConstant() {
        PsiBuilder.Marker complexConstant = mark();
        expect(LPAR, "( expected");
        parseExpression();
        expect(COMMA, ", expected");
        parseExpression();
        expect(RPAR, ") expected");
        complexConstant.done(COMPLEX_CONSTANT);
    }

    private void parseOneTokenExpression(FortranNodeType type) {
        PsiBuilder.Marker mark = mark();
        advance();
        mark.done(type);
    }

}
