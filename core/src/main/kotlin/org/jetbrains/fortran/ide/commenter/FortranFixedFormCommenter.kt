package org.jetbrains.fortran.ide.commenter

import com.intellij.codeInsight.generation.CommenterDataHolder
import com.intellij.codeInsight.generation.SelfManagingCommenter
import com.intellij.lang.Commenter
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil

class FortranFixedFormCommenter : Commenter, SelfManagingCommenter<CommenterDataHolder> {
    override fun getCommentedBlockCommentPrefix() = null
    override fun getCommentedBlockCommentSuffix() = null
    override fun getBlockCommentPrefix() = null
    override fun getBlockCommentSuffix() = null
    override fun getLineCommentPrefix() = "!"

    override fun createLineCommentingState(startLine: Int,
                                           endLine: Int,
                                           document: Document,
                                           file: PsiFile): CommenterDataHolder? = null

    override fun createBlockCommentingState(selectionStart: Int,
                                            selectionEnd: Int,
                                            document: Document,
                                            file: PsiFile): CommenterDataHolder? = null

    override fun commentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
        document.deleteString(offset, offset + 1)
        document.insertString(offset, lineCommentPrefix)
    }

    override fun uncommentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
        document.deleteString(offset, offset + 1)
        document.insertString(offset, " ")
    }

    override fun isLineCommented(line: Int, offset: Int, document: Document, data: CommenterDataHolder): Boolean {
        val realLineStartOffset = document.getLineStartOffset(document.getLineNumber(offset))

        return   ((offset == realLineStartOffset) && (CharArrayUtil.regionMatches(document.charsSequence, offset, offset+1, "c")
                    || CharArrayUtil.regionMatches(document.charsSequence, offset, offset+1, "C")
                    || CharArrayUtil.regionMatches(document.charsSequence, offset, offset+1, "c")
                    || CharArrayUtil.regionMatches(document.charsSequence, offset, offset+1, "*")))
                || ((offset - realLineStartOffset != 5) && CharArrayUtil.regionMatches(document.charsSequence, offset, offset+1, "!"))
    }

    override fun getCommentPrefix(line: Int, document: Document, data: CommenterDataHolder): String? = "c"

    override fun getBlockCommentRange(selectionStart: Int,
                                      selectionEnd: Int,
                                      document: Document,
                                      data: CommenterDataHolder): TextRange? {
        throw UnsupportedOperationException()
    }

    override fun getBlockCommentPrefix(selectionStart: Int, document: Document, data: CommenterDataHolder): String? = null

    override fun getBlockCommentSuffix(selectionEnd: Int, document: Document, data: CommenterDataHolder): String? = null

    override fun uncommentBlockComment(startOffset: Int, endOffset: Int, document: Document, data: CommenterDataHolder) {
        throw UnsupportedOperationException()
    }

    override fun insertBlockComment(startOffset: Int, endOffset: Int, document: Document, data: CommenterDataHolder): TextRange {
        throw UnsupportedOperationException()
    }
}