package org.jetbrains.fortran.ide.intentions

import com.intellij.analysis.AnalysisScope
import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.LowPriorityAction
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.file.PsiFileImplUtil
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranFixedFormConverter : IntentionAction, LowPriorityAction {
    override fun getText(): String {
        return familyName
    }

    override fun getFamilyName(): String {
        return "Convert to Free Form Source"
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor?, file: PsiFile) {
        if (!FileModificationService.getInstance().preparePsiElementForWrite(file)) return
        val scope = getScope(file)
        val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return

        runWriteAction {
            var oldTextStart = 0
            val builder = StringBuilder()
            if (scope != null) {

                PsiTreeUtil.findChildrenOfType(file, PsiComment::class.java).forEach{
                    builder.append(document.charsSequence.subSequence(oldTextStart, it.textOffset))
                    if (it.tokenType == FortranTokenType.LINE_COMMENT) {
                        builder.append("!")
                        oldTextStart = it.textOffset + 1
                    } else {
                        builder.append("& \n")
                        oldTextStart = it.textRange.endOffset
                    }
                }
                builder.append(document.charsSequence.subSequence(oldTextStart, document.charsSequence.lastIndex))
                document.setText(builder)
            }

            val newFile = PsiFileImplUtil.setName(file, file.name.substringBeforeLast('.') + "." + FortranFileType.defaultExtension)

            PsiDocumentManager.getInstance(project).commitAllDocuments()

            CodeStyleManagerImpl(project).reformatText(newFile, 0, newFile.textLength)
        }
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
