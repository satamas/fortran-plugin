package org.jetbrains.fortran.ide.typing

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.TokenType
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

    private fun isWordBrace(iterator: HighlighterIterator, fileText: CharSequence, left: Boolean): Boolean {
        val tokenText = fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase()
        if (left && tokenText in WORD_LEFT_BRACE) {
            if (aVariableOrType(iterator)) return false
            if (typeKeywordInSelectTypeConstruct(tokenText, fileText, iterator)) return false
            // end is not before us
            if (tokenText != "then") {
                return if (tokenText == "do") {
                    val label = labeledDo(iterator, fileText)
                    notAfterEnd(iterator, fileText) && (label == 0 || labeledDoHasEnd(iterator, fileText, label))
                } else {
                    notAfterEnd(iterator, fileText) && notWhereOrForallStmt(tokenText, iterator)
                }
            }
            return true
        }
        if (!left && tokenText in WORD_RIGHT_BRACE) {
            if (aVariableOrType(iterator)) return false
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
            if (iterator.atEnd()) return true
            while (iterator.tokenType != FortranTokenType.WORD) {
                if (iterator.tokenType === EOL) return true
                iterator.retreat()
                count++
                if (iterator.atEnd()) return true
            }

            return fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase() != "end"
        } finally {
            advanceIteratorBack(iterator, count)
        }
    }


    private fun elseIsNotLastKeyWord(iterator: HighlighterIterator, fileText: CharSequence): Boolean {
        var count = 1
        try {
            iterator.advance()
            if (iterator.atEnd()) return false
            while (iterator.tokenType != FortranTokenType.WORD) {
                if (iterator.tokenType === EOL) return false
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            val wordText = fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase()
            return wordText == "if" || wordText == "where"
        } finally {
            retreatIteratorBack(iterator, count)
        }
    }

    private fun typeKeywordInSelectTypeConstruct(tokenText: String, fileText: CharSequence, iterator: HighlighterIterator): Boolean {
        if (tokenText != "type" && tokenText != "class") return false

        return typeAfterSelect(fileText, iterator) || typeBeforeIsOrDefault(fileText, iterator)
    }

    private fun typeAfterSelect(fileText: CharSequence, iterator: HighlighterIterator): Boolean {
        var count = 1
        try {
            iterator.retreat()
            if (iterator.atEnd()) return false
            while (iterator.tokenType != FortranTokenType.WORD) {
                if (iterator.tokenType === EOL) return false
                iterator.retreat()
                count++
                if (iterator.atEnd()) return false
            }

            return (fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase() == "select")
        } finally {
            advanceIteratorBack(iterator, count)
        }
    }

    private fun typeBeforeIsOrDefault(fileText: CharSequence, iterator: HighlighterIterator): Boolean {
        var count = 1
        try {
            iterator.advance()
            if (iterator.atEnd()) return false
            while (iterator.tokenType != FortranTokenType.WORD) {
                if (iterator.tokenType === EOL) return false
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            val text = fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase()
            return (text == "is" || text == "default")
        } finally {
            retreatIteratorBack(iterator, count)
        }
    }

    private fun notWhereOrForallStmt(tokenText: String, iterator: HighlighterIterator): Boolean {
        if (tokenText != "where" && tokenText != "forall") return true
        var count = 0
        var braceCount = 1
        try {
            // need open parenthesis
            while (iterator.tokenType != LPAR) {
                if (iterator.tokenType === EOL) return false
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            iterator.advance()
            count++
            if (iterator.atEnd()) return false
            // need all parenthesis to be closed
            while (braceCount > 0) {
                if (iterator.tokenType === EOL) return false
                if (iterator.tokenType === LPAR) braceCount++
                if (iterator.tokenType === RPAR) braceCount--
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            // this might be the end of line
            while (iterator.tokenType != EOL) {
                if (iterator.tokenType === FortranTokenType.WORD) return false
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            return true
        } finally {
            retreatIteratorBack(iterator, count)
        }
    }

    // we are variable if next token is =, =>
    // type if somewhere later in this line is ::
    private fun aVariableOrType(iterator: HighlighterIterator): Boolean {
        var count = 1
        try {
            // simple check
            iterator.advance()
            if (iterator.atEnd()) return false
            while (iterator.tokenType === TokenType.WHITE_SPACE) {
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            if (iterator.tokenType === EQ || iterator.tokenType === POINTER_ASSMNT || iterator.tokenType === LBRACKET) {
                return true
            }
            if (iterator.tokenType != LPAR) {
                return false
            }

            // difficult check
            var braceCount = 0
            while (iterator.tokenType != EOL) {
                if (iterator.tokenType === LPAR) braceCount++
                if (iterator.tokenType === RPAR) braceCount--
                if (braceCount == 0 && (iterator.tokenType === EQ || iterator.tokenType === POINTER_ASSMNT
                        || iterator.tokenType === COLONCOLON)) {
                    return true
                }
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
            return false
        } finally {
            retreatIteratorBack(iterator, count)
        }
    }

    private fun labeledDo(iterator: HighlighterIterator, fileText: CharSequence): Int {
        var count = 1
        try {
            iterator.advance()
            if (iterator.atEnd()) return 0
            while (iterator.tokenType === TokenType.WHITE_SPACE) {
                iterator.advance()
                count++
                if (iterator.atEnd()) return 0
            }
            if (iterator.atEnd()) return 0
            return if (iterator.tokenType === INTEGERLITERAL)
                    fileText.subSequence(iterator.start, iterator.end).toString().toInt()
                else 0
        } finally {
            retreatIteratorBack(iterator, count)
        }
    }

    private fun labeledDoHasEnd(iterator: HighlighterIterator, fileText: CharSequence, label: Int): Boolean {
        var count = 0
        try {
            while (true) {
                // string end
                while (iterator.tokenType != EOL) {
                    iterator.advance()
                    count++
                    if (iterator.atEnd()) return false
                }
                if (iterator.atEnd()) return false
                while (iterator.tokenType === TokenType.WHITE_SPACE || iterator.tokenType === EOL) {
                    iterator.advance()
                    count++
                    if (iterator.atEnd()) return false
                }

                if (iterator.tokenType === INTEGERLITERAL
                        && label == fileText.subSequence(iterator.start, iterator.end).toString().toInt()) {
                    while (iterator.tokenType != FortranTokenType.WORD && iterator.tokenType != EOL) {
                        if (iterator.tokenType === EOL) return false
                        iterator.advance()
                        count++
                        if (iterator.atEnd()) return false
                    }
                    val tokenText = fileText.subSequence(iterator.start, iterator.end).toString().toLowerCase()
                    return (tokenText == "end" || tokenText == "enddo")
                }
                iterator.advance()
                count++
                if (iterator.atEnd()) return false
            }
        } finally {
            retreatIteratorBack(iterator, count)
        }
    }

    private fun advanceIteratorBack(iterator: HighlighterIterator, count : Int) {
        var localCount = count
        while (localCount-- > 0) {
            iterator.advance()
        }
    }

    private fun retreatIteratorBack(iterator: HighlighterIterator, count : Int) {
        var localCount = count
        while (localCount-- > 0) {
            iterator.retreat()
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
                "endprocedure",
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
                "else",
                "elseif"
        )
    }
}


