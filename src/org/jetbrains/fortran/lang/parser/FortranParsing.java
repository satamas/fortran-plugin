package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import static org.jetbrains.fortran.lang.FortranNodeTypes.FORTRAN_FILE;
import static org.jetbrains.fortran.lang.FortranNodeTypes.PRINT_STATEMENT;
import static org.jetbrains.fortran.lang.FortranNodeTypes.PROGRAM;
import static org.jetbrains.fortran.lang.lexer.FortranTokens.*;

public class FortranParsing extends AbstractFortranParsing {
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
            parsePrintStatement();
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
        expect(COMMA, "");
        parseString();
        printStatementElement.done(PRINT_STATEMENT);
    }

    private void parseString() {
        expect(OPENING_QUOTE, "");
        while (!eof() && at(REGULAR_STRING_PART)){
            advance();
        }
        expect(CLOSING_QUOTE, "");
    }

}
