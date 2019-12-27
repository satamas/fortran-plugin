package org.jetbrains.fortran.lang.preprocessor

import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenSets
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranMacro(val name: String) {
    companion object {
        fun parseFromDirectiveContent(content: CharSequence): FortranMacro? {
            val lexer = FortranLexer(false)
            lexer.start(content)
            while (FortranTokenSets.WHITE_SPACES.contains(lexer.tokenType)) {
                lexer.advance()
            }
            if (lexer.tokenType == FortranTokenType.WORD) {
                return FortranMacro(lexer.tokenText)
            }
            return null
        }
    }
}