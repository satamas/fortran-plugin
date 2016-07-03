package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import static org.jetbrains.fortran.lang.FortranNodeTypes.*;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.*;

public class FortranParsing extends AbstractFortranParsing {
    private final FortranExpressionParsing expressionParsing = new FortranExpressionParsing(builder);

    public FortranParsing(WhitespaceAwarePsiBuilder builder) {
        super(builder);
    }

    public void parseFile(IElementType root) {
        final PsiBuilder.Marker rootMarker = mark();
        while (!eof()) {
            if (at(PROGRAM_KEYWORD)) {
                parseProgram();
            } else {
                errorAndAdvance("Program expected");
            }
        }
        rootMarker.done(FORTRAN_FILE);
    }

    private void parseProgram() {
        assert (at(PROGRAM_KEYWORD));
        PsiBuilder.Marker programElement = mark();
        advance();
        expect(IDENTIFIER, "Program name expected");
        while (!eof() && !at(END_KEYWORD)) {
            if (at(IMPLICIT_KEYWORD)) {
                parseImplicitStatement();
            } else if (at(PRINT_KEYWORD)) {
                parsePrintStatement();
            } else {
                expressionParsing.parseStatement();
            }
        }
        expect(END_KEYWORD, "End of program expected");
        expect(PROGRAM_KEYWORD, "End of program expected");
        expect(IDENTIFIER, "Program name expected");
        programElement.done(PROGRAM);
    }

    private void parseImplicitStatement() {
        assert at(IMPLICIT_KEYWORD);
        labelDefinition();
        PsiBuilder.Marker printStatementElement = mark();
        advance();
        implicitSpecificationList();
        endOfStatement();
        printStatementElement.done(IMPLICIT_STATEMENT);
    }

    private void implicitSpecificationList() {
        if(at(NONE_KEYWORD)){
            implicitSpecification();
            return;
        }

        implicitSpecification();
        while (!eof() && at(COMMA)) {
            advance();
            implicitSpecification();
        }
    }

    private void implicitSpecification() {
        PsiBuilder.Marker implicitSpecification = mark();
        if (at(NONE_KEYWORD)) {
            advance();
        } else if(atSet(FortranExpressionParsing.TYPE_FIRST)) {
            typeSpecification();
            letterRangeSpecificationsList();
        } else {
            error("Implicit specification expected");
        }
        implicitSpecification.done(IMPLICIT_SPECIFICATION);
    }

    private void letterRangeSpecificationsList() {
        PsiBuilder.Marker letterSpecificationList = mark();
        expect(LPAR, "Letter specification expected");
        letterRangeSpecification();
        while (!eof() && at(COMMA)) {
            advance();
            letterRangeSpecification();
        }
        expect(RPAR, ") expected");
        letterSpecificationList.done(LETTER_RANGE_LIST);
    }

    private void letterRangeSpecification() {
        PsiBuilder.Marker letterRange = mark();
        letterSpecification();
        if (at(MINUS)) {
            advance();
            letterSpecification();
        }
        letterRange.done(LETTER_RANGE);
    }

    private void letterSpecification() {
        if (at(IDENTIFIER)) {
            String letter = builder.getTokenText();
            if (letter == null || letter.length() != 1) {
                error("Single letter expected");
            } else {
                advance();
            }
        } else {
            error("Letter range expected");
        }
    }

    private void typeSpecification() {
        if (!atSet(
                INTEGER_KEYWORD,
                REAL_KEYWORD,
                DOUBLE_KEYWORD,
                COMPLEX_KEYWORD,
                CHARACTER_KEYWORD,
                LOGICAL_KEYWORD,
                TYPE_KEYWORD)) {
            error("Type specification expected");
        }
        PsiBuilder.Marker typeSpecification = builder.mark();
        IElementType typeKeyword = tt();
        advance();

        if (typeKeyword == DOUBLE_KEYWORD) {
            advance();
            expect(PRECISION_KEYWORD, "Precision keyword expected");
        }

        if (typeKeyword == CHARACTER_KEYWORD) {
            if (at(MUL)) {
                advance();
                characterLength();
            }
//          TODO: return F90 style char length
//            else {
//                characterSelector();
//            }
        }
        typeSpecification.done(TYPE_REFERENCE);
    }

    private void characterLength() {
        if (at(INTEGER_LITERAL)) {
            advance();
        } else if (at(LPAR)) {
            advance();
            typeParamValue();
            expect(RPAR, ") expected");
        } else {
            error("Character length expected");
        }
    }

    private void typeParamValue() {
        if (at(MUL)) {
            advance();
        } else if (atSet(FortranExpressionParsing.EXPRESSION_FIRST)) {
            expressionParsing.parseExpression();
        } else {
            error("Type parameter value expected");
        }
    }

    private void characterSelector() {
        if (!at(LPAR)) {
            return;
        }
        advance();

        boolean expectingKind = false;
        if (at(LEN_KEYWORD)) {
            advance();
            if (at(EQ)) {
                advance();
                typeParamValue();
            } else {
                error("= expected");
            }
            if (at(RPAR)) {
                return;
            }
            if (at(COMMA)) {
                advance();
                expectingKind = true;
            } else {
                error(", or ) expected");
            }
        }

        if (at(KIND_KEYWORD)) {
            advance();
            if (at(EQ)) {
                advance();
                expressionParsing.parseExpression();
            } else {
                error("= expected");
            }
        }
        if (atSet(FortranExpressionParsing.EXPRESSION_FIRST)) {
            expressionParsing.parseExpression();
        } else {
            if (expectingKind) {
                error("Kind specification ecpected");
            } else {
                error("Character selector expected");
            }
        }
    }

    private void labelDefinition() {
        if (!at(INTEGER_CONSTANT)) {
            return;
        }
        PsiBuilder.Marker labelDefinition = builder.mark();
        advance();
        labelDefinition.done(LABEL_DEFINITION);
    }

    private void endOfStatement() {
        while (!eof()) {
            if (at(SEMICOLON)) {
                advance();
                return;
            }
            if (builder.newlineBeforeCurrentToken()) {
                return;
            }
            errorAndAdvance("Unexpected tokens (use ';' to separate expressions on the same line)");
        }
    }

    private void parsePrintStatement() {
        assert (at(PRINT_KEYWORD));
        PsiBuilder.Marker printStatementElement = mark();
        advance();
        expect(MUL, "");

        if (at(COMMA)) {
            advance();
            PsiBuilder.Marker valueArgumentsList = mark();
            expressionParsing.parseExpression();
            while (!eof() && at(COMMA)) {
                advance();
                expressionParsing.parseExpression();
            }
            valueArgumentsList.done(VALUE_ARGUMENT_LIST);
        }

        endOfStatement();
        printStatementElement.done(PRINT_STATEMENT);
    }

}
