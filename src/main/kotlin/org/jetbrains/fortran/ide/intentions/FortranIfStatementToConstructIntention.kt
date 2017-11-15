package org.jetbrains.fortran.ide.intentions

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranIfStmt

class FortranIfStatementToConstructIntention : BaseElementAtCaretIntentionAction() {

    override fun getText() = "Convert If stmt to construct"
    override fun getFamilyName(): String = text

    private fun findApplicableContext(element: PsiElement) =
            PsiTreeUtil.getParentOfType(element, FortranIfStmt::class.java)

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        if (!FileModificationService.getInstance().preparePsiElementForWrite(element)) return
        val ifStmnt = findApplicableContext(element) ?: return
        val lineStartOffset = editor.caretModel.visualLineStart
        val indentString = CodeStyleManager.getInstance(project)!!.getLineIndent(editor.document, lineStartOffset)
        val constructText = "then\n$indentString    ${ifStmnt.actionStmt.text}\n${indentString}end if"
        runWriteAction {
                val deletedRange = ifStmnt.actionStmt.textRange
                editor.document.deleteString(deletedRange.startOffset, deletedRange.endOffset)
                editor.document.insertString(deletedRange.startOffset, constructText)
                editor.caretModel.moveToOffset(deletedRange.startOffset+4) // after then
        }

    }

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean
            = findApplicableContext(element) != null
}
