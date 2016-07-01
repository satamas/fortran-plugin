package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.lang.FortranNodeType;

import static org.jetbrains.fortran.lang.FortranNodeTypes.*;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.*;

public class FortranExpressionParsing extends AbstractFortranParsing {
    TokenSet TYPE_FIRST = TokenSet.create(
            REAL_KEYWORD, INTEGER_KEYWORD, TYPE_KEYWORD, LOGICAL_KEYWORD, COMPLEX_KEYWORD);

    static final TokenSet EXPRESSION_FIRST = TokenSet.create(
            LPAR,
            OPENING_QUOTE,
            INTEGER_LITERAL,
            IDENTIFIER
    );

    public enum Precedence {

        EXPONENTIATION(POWER) {
            @Override
            public void parseHigherPrecedence(FortranExpressionParsing parser) {
                parser.parsePostfixExpression();
            }
        },
        MULTIPLICATIVE(MUL, DIV),
        ADDITIVE(PLUS, MINUS),
        EQUALITY(EQEQ);

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

    public FortranExpressionParsing(PsiBuilder builder) {
        super(builder);
    }


    public void parseStatement() {
        if (atSet(TYPE_FIRST)) {
            parseTypeStatement();
        } else if (at(IDENTIFIER)) {
            parseAssignment();
        } else {
            errorAndAdvance("Expecting a statement");
        }
    }

    public void parseTypeStatement() {
        assert atSet(TYPE_FIRST);
        PsiBuilder.Marker variableDeclaration = builder.mark();
        PsiBuilder.Marker typeReference = builder.mark();
        advance();
        typeReference.done(TYPE_REFERENCE);

        if (!at(COLONCOLON)) {
            variableDeclaration.drop();
            error(":: expected");
            return;
        }

        advance();
        expect(IDENTIFIER, "Identifier expected");

        if (!at(EQ)) {
            variableDeclaration.done(VARIABLE_DECLARATION);
            return;
        }
        advance();
        parseExpression();
        variableDeclaration.done(VARIABLE_DECLARATION);
    }

    public void parseAssignment() {
        assert at(IDENTIFIER);
        PsiBuilder.Marker assignment = builder.mark();
        if (!at(EQ)) {
            error("= expected");
            assignment.drop();
            return;
        }
        advance();
        parseExpression();
        assignment.done(ASSIGNMENT_EXPRESSION);
    }

    public void parseExpression() {
        if (!atSet(EXPRESSION_FIRST)) {
            error("Expecting an expression");
            return;
        }
        parseBinaryExpression(Precedence.EQUALITY);
    }

    private void parsePostfixExpression() {
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

    private void parseString() {
        PsiBuilder.Marker stringLiteral = mark();
        assert at(OPENING_QUOTE);
        advance();
        while (!eof() && at(REGULAR_STRING_PART)) {
            advance();
        }
        expect(CLOSING_QUOTE, "\" expected");
        stringLiteral.done(STRING_LITERAL);
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
        if (at(INTEGER_LITERAL)) {
            parseOneTokenExpression(INTEGER_CONSTANT);
        } else {
            return false;
        }
        return true;
    }

    private void parseOneTokenExpression(FortranNodeType type) {
        PsiBuilder.Marker mark = mark();
        advance();
        mark.done(type);
    }

}
