package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import static org.jetbrains.fortran.lang.FortranNodeTypes.*;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.*;

public class FortranParsing extends AbstractFortranParsing {
    private final FortranExpressionParsing expressionParsing = new FortranExpressionParsing(builder);

    public FortranParsing(PsiBuilder builder) {
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
            if(at(PRINT_KEYWORD)) {
                parsePrintStatement();
            } else {
                errorAndAdvance("Statement expected");
            }
        }
        expect(END_KEYWORD, "End of program expected");
        expect(PROGRAM_KEYWORD, "End of program expected");
        expect(IDENTIFIER, "Program name expected");
        programElement.done(PROGRAM);
    }

    private void parsePrintStatement() {
        assert (at(PRINT_KEYWORD));
        PsiBuilder.Marker printStatementElement = mark();
        advance();
        expect(MUL, "");

        if(at(COMMA)) {
            advance();
            PsiBuilder.Marker valueArgumentsList = mark();
            expressionParsing.parseExpression();
            while (!eof() && at(COMMA)) {
                advance();
                expressionParsing.parseExpression();
            }
            valueArgumentsList.done(VALUE_ARGUMENT_LIST);
        }

        printStatementElement.done(PRINT_STATEMENT);
    }

}
