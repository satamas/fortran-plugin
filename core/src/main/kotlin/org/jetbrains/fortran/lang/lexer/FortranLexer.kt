package org.jetbrains.fortran.lang.lexer

import com.intellij.lexer.FlexAdapter
import java.io.Reader

class FortranLexer(fFixedForm: Boolean) : FlexAdapter(_FortranLexer(null as Reader?, fFixedForm)) {
    companion object {
        val INITIAL = _FortranLexer.YYINITIAL
    }
}
