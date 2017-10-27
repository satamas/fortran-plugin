package org.jetbrains.fortran.ide.intentions

import com.intellij.analysis.AnalysisScope
import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile

class FortranFixedFormConverter : IntentionAction, LowPriorityAction {
    override fun getText(): String {
        return familyName
    }

    override fun getFamilyName(): String {
        return "Convert to Free Form Source"
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        if (!FileModificationService.getInstance().preparePsiElementForWrite(file)) return
        val scope = getScope(file)
        val document = PsiDocumentManager.getInstance(project).getDocument(file)
        runWriteAction {
            if (scope != null) {
                val comments = PsiTreeUtil.findChildrenOfType(file, PsiComment::class.java)
                comments.forEach { reComment(document, it)}
            }

            file.virtualFile.rename(this, file.name.substringBeforeLast('.') + ".f90")
        }
    }

    private fun reComment(document : Document?, comment: PsiComment) {
        if (document == null) return
        document.deleteString(comment.textOffset, comment.textOffset + 1)
        document.insertString(comment.textOffset, "!")
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return (file is FortranFixedFormFile)
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    private fun getScope(file: PsiFile): AnalysisScope? {
        return AnalysisScope(file)
    }
}
