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
            isWordBrace(iterator, fileText, true)
        } else {
            false
        }
    }


    override fun isRBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean {
        if (fileType != FortranFileType && fileType != FortranFixedFormFileType) return false
        return if (iterator.tokenType == RPAR || iterator.tokenType == RBRACKET || iterator.tokenType == ARRAYRBR) {
            true
        } else if (iterator.tokenType == FortranTokenType.WORD) {
            isWordBrace(iterator, fileText, false)
        } else {
            false
        }
    }

    // тут нужно много качественных проверок, а то скобки разбредутся
    private fun isWordBrace(iterator: HighlighterIterator, fileText: CharSequence, left: Boolean): Boolean {
        val tokenText = fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase()
        if (left  && tokenText in WORD_LEFT_BRACE) {
            // end is not before us
            if (tokenText != "then") {
                return notAfterEnd(iterator, fileText)
                      && notWhereOrForallStmt(tokenText, iterator)
            }
            return true
        }
        if (!left && tokenText in WORD_RIGHT_BRACE) {
            if (tokenText == "else") {
                return elseIsNotLastKeyWord(iterator, fileText)
            }
            return true
        }
        return false
    }

    private fun notAfterEnd(iterator: HighlighterIterator, fileText: CharSequence): Boolean {
        var count = 1
        try {
            iterator.retreat()
            while (iterator.tokenType != FortranTokenType.WORD) {
                if (iterator.atEnd()) return true
                if (iterator.tokenType === EOL) return true
                iterator.retreat()
                count++
            }

            return fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase() != "end"
        } finally {
            while (count-- > 0) {
                iterator.advance()
            }
        }
    }

    private fun elseIsNotLastKeyWord(iterator: HighlighterIterator, fileText: CharSequence): Boolean {
        var count = 1
        try {
            iterator.advance()
            while (iterator.tokenType != FortranTokenType.WORD) {
                if (iterator.atEnd()) return false
                if (iterator.tokenType === EOL) return false
                iterator.advance()
                count++
            }
            val wordText = fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase()
            return wordText == "if" || wordText == "where"
        } finally {
            while (count-- > 0) {
                iterator.retreat()
            }
        }
    }

    private fun notWhereOrForallStmt(tokenText: String, iterator: HighlighterIterator): Boolean {
        if (tokenText != "where" && tokenText != "forall") return true
        var count = 0
        var braceCount = 1
        try {
            // need open parenthesis
            while (iterator.tokenType != LPAR) {
                if (iterator.atEnd()) return false
                if (iterator.tokenType === EOL) return false
                iterator.advance()
                count++
            }
            iterator.advance()
            count++
            // need all parenthesis to be closed
            while (braceCount > 0) {
                println(braceCount)
                if (iterator.atEnd()) return false
                if (iterator.tokenType === EOL) return false
                if (iterator.tokenType === LPAR)braceCount++
                if (iterator.tokenType === RPAR) braceCount--
                iterator.advance()
                count++
            }
            // this might be the end of line
            while (iterator.tokenType != EOL) {
                if (iterator.atEnd()) return false
                if (iterator.tokenType === FortranTokenType.WORD) return false
                iterator.advance()
                count++
            }
            return true
        } finally {
            while (count-- > 0) {
                iterator.retreat()
            }
        }
    }

    companion object {
        private val WORD_LEFT_BRACE: Array<String> = arrayOf(
                "program",
                "function",
                "subroutine",
                "block",
                "blockdata",
                "module",
                "submodule",
                "enum",
                "type",
                "interface",
                "associate",
                "critical",
                "forall",
                "select",
                "where",
                "do",
                "then"
        )

        private val WORD_RIGHT_BRACE: Array<String> = arrayOf(
                "end",
                "endprogram",
                "endfunction",
                "endsubroutine",
                "endblock",
                "endblockdata",
                "endmodule",
                "endsubmodule",
                "endenum",
                "endtype",
                "endinterface",
                "endassociate",
                "endcritical",
                "endforall",
                "endselect",
                "endwhere",
                "enddo",
                "endif",
                "else"
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
