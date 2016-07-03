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
            PsiBuilder.Marker marker = mark();
            IElementType statementType;
            parseLabelDefinition();

            if (at(IMPLICIT_KEYWORD)) {
                statementType = parseImplicitStatement();
            } else if (at(PARAMETER_KEYWORD)) {
                statementType = parseParameterStatement();
            } else if (at(PRINT_KEYWORD)) {
                statementType = parsePrintStatement();
            } else {
                statementType = expressionParsing.parseStatement();
            }

            parseEndOfStatement();

            if (statementType != null) {
                marker.done(statementType);
            } else {
                marker.drop();
            }
        }
        expect(END_KEYWORD, "End of program expected");
        expect(PROGRAM_KEYWORD, "End of program expected");
        expect(IDENTIFIER, "Program name expected");
        programElement.done(PROGRAM);
    }

    private IElementType parseImplicitStatement() {
        assert at(IMPLICIT_KEYWORD);
        advance();
        parseImplicitSpecificationList();
        return IMPLICIT_STATEMENT;
    }

    private IElementType parseParameterStatement() {
        assert at(PARAMETER_KEYWORD);
        advance();
        expect(LPAR, "Parameters list expected");
        parseParametersList();
        expect(RPAR, ") expected");
        return PARAMETER_STATEMENT;
    }

    private void parseParametersList() {
        parseParameter();
        while (!eof() && at(COMMA)){
            advance();
            parseParameter();
        }
    }

    private void parseParameter(){
        PsiBuilder.Marker parameter = mark();
        expect(IDENTIFIER, "Parameter name expected");
        expect(EQ, "= expected");
        expressionParsing.parseExpression();
        parameter.done(PARAMETER);
    }

    private void parseImplicitSpecificationList() {
        if (at(NONE_KEYWORD)) {
            parseImplicitSpecification();
            return;
        }

        parseImplicitSpecification();
        while (!eof() && at(COMMA)) {
            advance();
            parseImplicitSpecification();
        }
    }

    private void parseImplicitSpecification() {
        PsiBuilder.Marker implicitSpecification = mark();
        if (at(NONE_KEYWORD)) {
            advance();
        } else if (atSet(FortranExpressionParsing.TYPE_FIRST)) {
            parseTypeSpecification();
            parseLetterRangeSpecificationsList();
        } else {
            error("Implicit specification expected");
        }
        implicitSpecification.done(IMPLICIT_SPECIFICATION);
    }

    private void parseLetterRangeSpecificationsList() {
        PsiBuilder.Marker letterSpecificationList = mark();
        expect(LPAR, "Letter specification expected");
        parseLetterRangeSpecification();
        while (!eof() && at(COMMA)) {
            advance();
            parseLetterRangeSpecification();
        }
        expect(RPAR, ") expected");
        letterSpecificationList.done(LETTER_RANGE_LIST);
    }

    private void parseLetterRangeSpecification() {
        PsiBuilder.Marker letterRange = mark();
        parseLetterSpecification();
        if (at(MINUS)) {
            advance();
            parseLetterSpecification();
        }
        letterRange.done(LETTER_RANGE);
    }

    private void parseLetterSpecification() {
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

    private void parseTypeSpecification() {
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
                parseCharacterLength();
            }
//          TODO: return F90 style char length
//            else {
//                characterSelector();
//            }
        }
        typeSpecification.done(TYPE_REFERENCE);
    }

    private void parseCharacterLength() {
        if (at(INTEGER_LITERAL)) {
            advance();
        } else if (at(LPAR)) {
            advance();
            parseTypeParamValue();
            expect(RPAR, ") expected");
        } else {
            error("Character length expected");
        }
    }

    private void parseTypeParamValue() {
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
                parseTypeParamValue();
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

    private void parseLabelDefinition() {
        if (!at(INTEGER_LITERAL)) {
            return;
        }
        PsiBuilder.Marker labelDefinition = builder.mark();
        advance();
        labelDefinition.done(LABEL_DEFINITION);
    }

    private void parseEndOfStatement() {
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

    private IElementType parsePrintStatement() {
        assert (at(PRINT_KEYWORD));
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

        parseEndOfStatement();
        return PRINT_STATEMENT;
    }

}
