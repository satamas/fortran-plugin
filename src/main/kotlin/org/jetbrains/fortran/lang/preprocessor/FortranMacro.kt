package org.jetbrains.fortran.lang.preprocessor

import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranMacro(val type: FortranTokenType, val text: String) {

    val name: String?
        get() {
            val lexer = FortranLexer(false)
            lexer.start(text)
            while (FortranTokenType.WHITE_SPACES.contains(lexer.tokenType)) {
                lexer.advance()
            }
            if (lexer.tokenType == FortranTokenType.WORD) {
                return lexer.tokenText
            }
            return null
        }
}