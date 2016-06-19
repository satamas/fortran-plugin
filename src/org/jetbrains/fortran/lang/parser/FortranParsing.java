package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.fortran.lang.FortranNodeTypes;

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
        rootMarker.done(FortranNodeTypes.FORTRAN_FILE);
    }

    private void parseProgram() {
        assert (at(PROGRAM_KEYWORD));
        advance();

        expect(IDENTIFIER, "Program name expected");
        expect(END_KEYWORD, "End of program expected");
        expect(PROGRAM_KEYWORD, "End of program expected");
        expect(IDENTIFIER, "Program name expected");
    }
}
