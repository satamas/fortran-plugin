package org.jetbrains.fortran.ide.typing

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranBaseBraceMatcher : PairedBraceMatcher {

    override fun getPairs() = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean =
            true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS: Array<BracePair> = arrayOf(
                BracePair(FortranTypes.LPAR, FortranTypes.RPAR, false),
                BracePair(FortranTypes.LBRACKET, FortranTypes.RBRACKET, false),
                BracePair(FortranTypes.ARRAYLBR, FortranTypes.ARRAYRBR, false),
                BracePair(FortranTokenType.WORD, FortranTokenType.WORD, true)
        )
    }
}