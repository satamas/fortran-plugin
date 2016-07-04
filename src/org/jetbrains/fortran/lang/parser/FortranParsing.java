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
            PsiBuilder.Marker marker = builder.mark();
            parseLabelDefinition();
            if (at(PROGRAM_KEYWORD)) {
                marker.rollbackTo();
                parseProgram();
            } else if (at(FUNCTION_KEYWORD)) {
                marker.rollbackTo();
                parseFunctionOrSubroutine(true);
            } else if (at(SUBROUTINE_KEYWORD)) {
                marker.rollbackTo();
                parseFunctionOrSubroutine(false);
            } else if (at(BLOCK_KEYWORD)) {
                marker.rollbackTo();
                parseBlockData();
            } else if (atSet(FortranExpressionParsing.TYPE_FIRST)) {
                parseTypeSpecification();

                if (at(FUNCTION_KEYWORD)) {
                    marker.rollbackTo();
                    parseFunctionOrSubroutine(true);
                } else {
                    marker.rollbackTo();
                    parseProgram();
                }
            } else {
                errorAndAdvance("Top level declaration expected");
            }
        }
        rootMarker.done(FORTRAN_FILE);
    }

    private void parseFunctionOrSubroutine(boolean isFunction) {
        PsiBuilder.Marker function = mark();
        parseFunctionOrSubroutineStatement(isFunction);
        parseBody();
        parseEndStatement();
        function.done(isFunction ? FUNCTION : SUBROUTINE);
    }

    private void parseBlockData() {
        PsiBuilder.Marker function = mark();
        parseBlockDataStatement();
        parseBody();
        parseEndStatement();
        function.done(BLOCK_DATA);
    }

    private void parseBlockDataStatement() {
        PsiBuilder.Marker functionStatement = mark();
        parseLabelDefinition();
        assert at(BLOCK_KEYWORD);
        advance();
        expect(DATA_KEYWORD, "'data' expected");
        if (at(IDENTIFIER)) {
            advance();
        }
        parseEndOfStatement();
        functionStatement.done(BLOCK_DATA_STATEMENT);
    }

    private void parseProgram() {
        PsiBuilder.Marker programElement = mark();
        parseProgramStatement();
        parseBody();
        parseEndProgramStatement();
        programElement.done(PROGRAM);
    }

    private void parseProgramStatement() {
        PsiBuilder.Marker programStatement = mark();
        parseLabelDefinition();
        if (!at(PROGRAM_KEYWORD)) {
            programStatement.rollbackTo();
            return;
        }
        advance();
        expect(IDENTIFIER, "Program name expected");
        parseEndOfStatement();
        programStatement.done(PROGRAM_STATEMENT);
    }

    private void parseBody() {
        while (!eof()) {
            PsiBuilder.Marker marker = mark();
            IElementType statementType;
            parseLabelDefinition();

            if (at(IMPLICIT_KEYWORD)) {
                statementType = parseImplicitStatement();
            } else if (at(PARAMETER_KEYWORD)) {
                statementType = parseParameterStatement();
            } else if (at(PRINT_KEYWORD)) {
                statementType = parsePrintStatement();
            } else if (at(END_KEYWORD)) {
                marker.rollbackTo();
                break;
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
    }

    private boolean parseEndProgramStatement() {
        PsiBuilder.Marker endProgramStatement = mark();
        parseLabelDefinition();
        if (!at(END_KEYWORD)) {
            endProgramStatement.rollbackTo();
            return false;
        }
        advance();
        expect(PROGRAM_KEYWORD, "'Program' expected");
        if (at(IDENTIFIER)) {
            advance();
        }
        parseEndOfStatement();
        endProgramStatement.done(END_PROGRAM_STATEMENT);
        return true;
    }

    private void parseFunctionOrSubroutineStatement(boolean isFunction) {
        PsiBuilder.Marker functionStatement = mark();
        parseLabelDefinition();
        if (isFunction && atSet(FortranExpressionParsing.TYPE_FIRST)) {
            parseTypeSpecification();
        }
        assert atSet(FUNCTION_KEYWORD, SUBROUTINE_KEYWORD);
        advance();
        expect(IDENTIFIER, (isFunction ? "Function" : "Subroutine") + " name expected");
        if (at(LPAR)) {
            advance();
            parseParameters();
            expect(RPAR, ") expected");
        } else {
            error("Parameters list expected");
        }
        parseEndOfStatement();
        functionStatement.done(isFunction ? FUNCTION_STATEMENT : SUBROUTINE_STATEMENT);
    }

    private void parseParameters() {
        PsiBuilder.Marker params = mark();
        if (at(IDENTIFIER)) {
            advance();
        }
        while (at(COMMA)) {
            advance();
            expect(IDENTIFIER, "Parameter name expected");
        }
        params.done(PARAMS);
    }

    private void parseEndStatement() {
        PsiBuilder.Marker endFunctionStatement = mark();
        parseLabelDefinition();
        assert at(END_KEYWORD);
        advance();
        parseEndOfStatement();
        endFunctionStatement.done(END_STATEMENT);
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
        while (!eof() && at(COMMA)) {
            advance();
            parseParameter();
        }
    }

    private void parseParameter() {
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
