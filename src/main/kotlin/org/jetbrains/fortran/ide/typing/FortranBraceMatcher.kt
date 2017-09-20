package org.jetbrains.fortran.ide.typing

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType


class FortranBraceMatcher : PairedBraceMatcherAdapter(FortranBaseBraceMatcher(), FortranLanguage) {

    override fun isLBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean {
        if (fileType != FortranFileType && fileType != FortranFixedFormFileType) return false
        return if (iterator.tokenType == LPAR || iterator.tokenType == LBRACKET || iterator.tokenType == ARRAYLBR) {
            true
        } else if (iterator.tokenType == FortranTokenType.WORD) {
            isWordBrace(iterator, fileText, fileType, true)
        } else {
            false
        }
    }


    override fun isRBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean {
        if (fileType != FortranFileType && fileType != FortranFixedFormFileType) return false
        return if (iterator.tokenType == RPAR || iterator.tokenType == RBRACKET || iterator.tokenType == ARRAYRBR) {
            true
        } else if (iterator.tokenType == FortranTokenType.WORD) {
            isWordBrace(iterator, fileText, fileType, false)
        } else {
            false
        }
    }

    private fun isWordBrace(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType, left: Boolean): Boolean {
        if (left  && fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase() in WORD_LEFT_BRACE) return true
        if (!left && fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase() in WORD_RIGHT_BRACE) return true
        return false
    }

    companion object {
        private val WORD_LEFT_BRACE: Array<String> = arrayOf(
                "then"
        )

        private val WORD_RIGHT_BRACE: Array<String> = arrayOf(
                "endif"
        )
    }

}

private class FortranBaseBraceMatcher : PairedBraceMatcher {

    override fun getPairs() = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean =
            true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS: Array<BracePair> = arrayOf(
                BracePair(LPAR, RPAR, false),
                BracePair(LBRACKET, RBRACKET, false),
                BracePair(ARRAYLBR, ARRAYRBR, false),
                BracePair(FortranTokenType.WORD, FortranTokenType.WORD, true)
        )
    }
}
