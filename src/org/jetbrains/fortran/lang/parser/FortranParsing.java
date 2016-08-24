package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import static org.jetbrains.fortran.lang.FortranNodeTypes.*;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.*;
import static org.jetbrains.fortran.lang.parser.FortranExpressionParsing.EXPRESSION_FIRST;
import static org.jetbrains.fortran.lang.parser.FortranExpressionParsing.TYPE_FIRST;

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
            } else if (atSet(TYPE_FIRST)) {
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
            } else if (at(FORMAT_KEYWORD)) {
                statementType = parseFormatStatement();
            } else if (at(ENTRY_KEYWORD)) {
                statementType = parseEntryStatement();
            } else if (at(PRINT_KEYWORD)) {
                statementType = parsePrintStatement();
            } else if (at(COMMON_KEYWORD)) {
                statementType = parseCommonStatement();
            } else if (at(DATA_KEYWORD)) {
                statementType = parseDataStatement();
            } else if (at(SAVE_KEYWORD)) {
                statementType = parseSaveStatement();
            } else if (at(DIMENSION_KEYWORD)) {
                statementType = parseDimensionStatement();
            } else if (at(EQUIVALENCE_KEYWORD)) {
                statementType = parseEquivalenceStatement();
            } else if (at(INTRINSIC_KEYWORD)) {
                statementType = parseIntrinsicStatement();
            } else if (at(EXTERNAL_KEYWORD)) {
                statementType = parseExternalStatement();
            } else if (at(END_KEYWORD)) {
                marker.rollbackTo();
                break;
            } else if (at(IF_KEYWORD)) {
                statementType = parseIfStatement();
            } else if (atSet(TYPE_FIRST)) {
                statementType = parseTypeStatement();
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
        if (isFunction && atSet(TYPE_FIRST)) {
            parseTypeSpecification();
        }
        assert atSet(FUNCTION_KEYWORD, SUBROUTINE_KEYWORD);
        advance();
        expect(IDENTIFIER, (isFunction ? "Function" : "Subroutine") + " name expected");
        parseParameters();
        parseEndOfStatement();
        functionStatement.done(isFunction ? FUNCTION_STATEMENT : SUBROUTINE_STATEMENT);
    }

    private void parseParameters() {
        if (at(LPAR)) {
            advance();
            PsiBuilder.Marker params = mark();
            if (at(IDENTIFIER)) {
                advance();
            }
            while (at(COMMA)) {
                advance();
                expect(IDENTIFIER, "Parameter name expected");
            }
            params.done(PARAMS);
            expect(RPAR, ") expected");
        } else {
            error("Parameters list expected");
        }
    }

    private void parseEndStatement() {
        PsiBuilder.Marker endFunctionStatement = mark();
        parseLabelDefinition();
        assert at(END_KEYWORD);
        advance();
        parseEndOfStatement();
        endFunctionStatement.done(END_STATEMENT);
    }

    private IElementType parseDataStatement() {
        assert at(DATA_KEYWORD);
        advance();
        parseDataList();
        return DATA_STATEMENT;
    }

    private void parseDataList() {
        parseDataStatementSet();
        while (!eof()) {
            if (at(COMMA)) {
                advance();
                if (!at(IDENTIFIER) && !at(LPAR)) {
                    error("Data statement set expected");
                    break;
                }
            } else {
                if (!at(IDENTIFIER) && !at(LPAR)) break;
            }
            parseDataStatementSet();
        }
    }

    private void parseDataStatementSet() {
        parseDataStatementObjectList();
        expect(DIV, "/ expected");
        parseDataStatementValueList();
        expect(DIV, "/ expected");
    }

    private void parseDataStatementObjectList() {
        parseDataStatementObject();
        while (!eof() && at(COMMA)) {
            advance();
            parseDataStatementObject();
        }
    }

    private void parseDataStatementObject() {
        if (at(IDENTIFIER)) {
            parseVariable();
        } else if (at(LPAR)) {
            parseDataImpliedDo();
        } else {
            error("Data statement object expected");
        }
    }

    private void parseDataImpliedDo() {
        assert at(LPAR);
        advance();
        parseImpliedDoObject();
        while (!eof() && at(COMMA)) {
            advance();
            if (at(IDENTIFIER) && builder.lookAhead(1) == EQ) break;
            parseImpliedDoObject();
        }

        expect(IDENTIFIER, "Identifier expected");
        expect(EQ, "= expected");
        expressionParsing.parseExpression();
        expect(COMMA, ", expected");
        expressionParsing.parseExpression();
        if (at(COMMA)) {
            advance();
            expressionParsing.parseExpression();
        }
        expect(RPAR, ") expected");
    }

    private void parseImpliedDoObject() {
        PsiBuilder.Marker dataImpliedDoObject = mark();
        if (at(LPAR)) {
            parseDataImpliedDo();
        } else {
            parseArrayElement();
        }
        dataImpliedDoObject.done(DATA_IMPLIED_DO_OBJECT);
    }

    private void parseArrayElement() {
        expect(IDENTIFIER, "Identifier expected");
        expect(LPAR, "( expected");
        parseSectionSubscriptList();
        expect(RPAR, ") expected");
    }

    private void parseSectionSubscriptList() {
        parseSubscriptTriplet(true);
        while (!eof() && at(COMMA)) {
            advance();
            parseSubscriptTriplet(true);
        }
    }

    private void parseDataStatementValueList() {
        parseDataStatementValue();
        while (!eof() && at(COMMA)) {
            advance();
            parseDataStatementValue();
        }
    }

    private void parseDataStatementValue() {
        PsiBuilder.Marker dataStatementValue = mark();
        if (at(INTEGER_LITERAL) || at(IDENTIFIER)) {
            PsiBuilder.Marker marker = mark();
            IElementType type = tt();
            advance();
            marker.done(type == INTEGER_LITERAL ? INTEGER_CONSTANT : REFERENCE_EXPRESSION);
            if (at(MUL)) {
                advance();
                expressionParsing.parsePrefixExpression();
            }
        } else {
            expressionParsing.parsePrefixExpression();
        }
        dataStatementValue.done(DATA_STATEMENT_VALUE);
    }

    private IElementType parseEquivalenceStatement() {
        assert at(EQUIVALENCE_KEYWORD);
        advance();
        parseEquivalenceSetList();
        return EQUIVALENCE_STATEMENT;
    }

    private void parseEquivalenceSetList() {
        parseEquivalenceSet();
        while (!eof() && at(COMMA)) {
            advance();
            parseEquivalenceSet();
        }
    }

    private void parseEquivalenceSet() {
        expect(LPAR, "( expected");
        PsiBuilder.Marker marker = mark();
        parseVariable();
        expect(COMMA, ", expected");
        parseVariable();
        while (!eof() && at(COMMA)) {
            advance();
            parseVariable();
        }
        marker.done(EQUIVALENCE_SET);
        expect(RPAR, ") expected");
    }

    private IElementType parseExternalStatement() {
        assert at(EXTERNAL_KEYWORD);
        advance();
        expect(IDENTIFIER, "Procedure name expected");
        while (!eof() && at(COMMA)) {
            advance();
            expect(IDENTIFIER, "Procedure name expected");
        }
        return EXTERNAL_STATEMENT;
    }

    private IElementType parseIntrinsicStatement() {
        assert at(INTRINSIC_KEYWORD);
        advance();
        expect(IDENTIFIER, "Procedure name expected");
        while (!eof() && at(COMMA)) {
            advance();
            expect(IDENTIFIER, "Procedure name expected");
        }
        return INTRINSIC_STATEMENT;
    }

    private IElementType parseCommonStatement() {
        assert at(COMMON_KEYWORD);
        advance();
        parseCommonList();
        return COMMON_STATEMENT;
    }

    private void parseCommonList() {
        PsiBuilder.Marker commonBlockList = builder.mark();
        parseCommonBlock();
        while (at(COMMA) || at(DIV)) {
            if (at(COMMA)) {
                advance();
            }
            parseCommonBlock();
        }
        commonBlockList.done(COMMON_BLOCK_LIST);
    }

    private void parseCommonBlock() {
        PsiBuilder.Marker commonBlock = builder.mark();
        if (at(DIV)) {
            parseCommonBlockName(false);
        }
        parseCommonDeclarationList();
        commonBlock.done(COMMON_BLOCK);
    }

    private void parseCommonBlockName(boolean nameRequired) {
        assert at(DIV);
        PsiBuilder.Marker commonBlockName = builder.mark();
        advance();
        if (nameRequired && !at(IDENTIFIER)) {
            error("Identifier expected");
        }
        if (at(IDENTIFIER)) {
            advance();
        }
        expect(DIV, "/ expected");
        commonBlockName.done(COMMON_BLOCK_NAME);
    }

    private void parseCommonDeclarationList() {
        parseEntityDeclaration(false, true);
        while (!eof() && at(COMMA)) {
            PsiBuilder.Marker marker = mark();
            advance();
            if (!at(IDENTIFIER)) {
                marker.rollbackTo();
                return;
            }

            marker.drop();
            parseEntityDeclaration(false, true);
        }
    }

    private IElementType parseDimensionStatement() {
        assert at(DIMENSION_KEYWORD);
        advance();
        parseEntityDeclarationList(false);
        return DIMENSION_STATEMENT;
    }

    private IElementType parseSaveStatement() {
        assert at(SAVE_KEYWORD);
        advance();
        parseSavedEntityList();
        return SAVE_STATMENT;
    }

    private void parseSavedEntityList() {
        if (!at(IDENTIFIER) && !at(DIV)) return;
        parseSavedEntity();
        while (!eof() && at(COMMA)) {
            advance();
            parseSavedEntity();
        }
    }

    private void parseSavedEntity() {
        if (at(DIV)) {
            parseCommonBlockName(true);
        } else {
            parseEntityDeclaration(false, false);
        }
    }

    private IElementType parseImplicitStatement() {
        assert at(IMPLICIT_KEYWORD);
        advance();
        parseImplicitSpecificationList();
        return IMPLICIT_STATEMENT;
    }

    private IElementType parseEntryStatement() {
        assert at(ENTRY_KEYWORD);
        advance();
        expect(IDENTIFIER, "Entry name expected");
        parseParameters();
        return ENTRY_STATEMENT;
    }

    private IElementType parseFormatStatement() {
        assert at(FORMAT_KEYWORD);
        advance();
        expect(LPAR, "Format specification expected");
        parseFormatSpecification();
        expect(RPAR, ") expected");
        return FORMAT_STATEMENT;
    }

    private IElementType parseTypeStatement() {
        assert atSet(TYPE_FIRST);
        parseTypeSpecification();
        parseEntityDeclarationList(true);
        return TYPE_DECLARATION_STATEMENT;
    }

    private void parseEntityDeclarationList(boolean charLengthAllowed) {
        parseEntityDeclaration(charLengthAllowed, true);
        while (!eof() && at(COMMA)) {
            advance();
            parseEntityDeclaration(charLengthAllowed, true);
        }
    }

    private void parseEntityDeclaration(boolean charLengthAllowed, boolean arraySpecifierAllowed) {
        PsiBuilder.Marker entityDeclaration = mark();
        expect(IDENTIFIER, "Identifier expected");
        if (arraySpecifierAllowed && at(LPAR)) {
            PsiBuilder.Marker shapeSpecification = mark();
            advance();
            parseArraySpecification();
            expect(RPAR, ") expected");
            shapeSpecification.done(ARRAY_SPECIFICATION);
        }
        if (charLengthAllowed && at(MUL)) {
            advance();
            parseCharacterLength();
        }
        entityDeclaration.done(ENTITY_DECLARATION);
    }

    private void parseArraySpecification() {
        parseShapeSpecification();
        while (!eof() && at(COMMA)) {
            advance();
            parseShapeSpecification();
        }
    }

    private void parseShapeSpecification() {
        PsiBuilder.Marker shapeSpecification = mark();
        if (at(MUL)) {
            advance();
            shapeSpecification.done(SIZE_SPECIFICATION);
            return;
        }
        expressionParsing.parseExpression();
        if (!at(COLON)) {
            shapeSpecification.done(SIZE_SPECIFICATION);
            return;
        }
        advance();
        if (at(MUL)) {
            advance();
            shapeSpecification.done(SIZE_SPECIFICATION);
            return;
        }
        expressionParsing.parseExpression();
        shapeSpecification.done(SIZE_SPECIFICATION);
    }

    private IElementType parseParameterStatement() {
        assert at(PARAMETER_KEYWORD);
        advance();
        expect(LPAR, "Parameters list expected");
        parseParametersList();
        expect(RPAR, ") expected");
        return PARAMETER_STATEMENT;
    }

    private IElementType parseIfStatement() {
        assert at(IF_KEYWORD);
        advance();
        expect(LPAR, "( expected");
        expressionParsing.parseExpression();
        expect(RPAR, ") expected");

        parseLabelReference();
        expect(COMMA, ", expected");
        parseLabelReference();
        expect(COMMA, ", expected");
        parseLabelReference();
        return IF_STATMENT;
    }

    private void parseLabelReference() {
        PsiBuilder.Marker marker = mark();
        if(at(INTEGER_LITERAL)){
            advance();
            marker.done(LABEL_REFERENCE);
        } else {
            marker.drop();
        }
    }

    private void parseFormatSpecification() {
        PsiBuilder.Marker parameter = mark();
        while (!eof() && !at(RPAR)) {
            advance();
        }
        parameter.done(FORMAT_SPECIFICATION);
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
        } else if (atSet(TYPE_FIRST)) {
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

    private void parseVariable() {
        PsiBuilder.Marker marker = mark();
        expect(IDENTIFIER, "Identifier expected");
        if (at(LPAR)) {
            parseSubscriptList();
        }
        if (at(LPAR)) {
            parseSubstringRange();
        }
        marker.done(VARIABLE);
    }

    private void parseSubscriptList() {
        assert at(LPAR);
        PsiBuilder.Marker marker = mark();
        advance();

        //Semicolon means that this is substring range
        if (at(SEMICOLON)) {
            marker.rollbackTo();
            return;
        }
        expressionParsing.parseExpression();

        //Semicolon means that this is substring range
        if (at(SEMICOLON)) {
            marker.rollbackTo();
            return;
        }

        while (!eof() && at(COMMA)) {
            advance();
            expressionParsing.parseExpression();
        }
        expect(RPAR, ") expected");
        marker.done(SUBSCRIPT_LIST);
    }

    private void parseSubstringRange() {
        assert at(LPAR);
        advance();
        PsiBuilder.Marker marker = mark();
        parseSubscriptTriplet(false);
        marker.done(SUBSTRING_RANGE);
        expect(RPAR, ") expected");
    }

    private void parseSubscriptTriplet(boolean allowSingleExpression) {
        if (at(COLON)) {
            advance();
            if (atSet(EXPRESSION_FIRST)) {
                expressionParsing.parseExpression();
            }
        } else {
            expressionParsing.parseExpression();

            if (at(COLON)) {
                advance();
            } else {
                if (!allowSingleExpression) error(": expected");
                return;
            }
            if (atSet(EXPRESSION_FIRST)) {
                expressionParsing.parseExpression();
            }
        }
    }


}
