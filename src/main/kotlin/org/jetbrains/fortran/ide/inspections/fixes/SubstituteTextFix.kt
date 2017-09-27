package org.jetbrains.fortran.ide.inspections.fixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import org.jetbrains.fortran.lang.psi.FortranCompositeElement

/**
 * Fix that removes the given range from the document and places a text onto its place.
 * @param text The text that will be placed starting from `range.startOffset`. If `null`, no text will be inserted.
 * @param fixName The name to use for the fix instead of the default one to better fit the inspection.
 */
class SubstituteTextFix(
        private val startRangeElementPointer: SmartPsiElementPointer<FortranCompositeElement>,
        private val endRangeElementPointer: SmartPsiElementPointer<FortranCompositeElement>,
        private val text: String?,
        private val fixName: String
) : LocalQuickFix {
    override fun getName() = fixName
    override fun getFamilyName() = "Substitute one text to another"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val startRangeElement = startRangeElementPointer.element
        val endRangeElement = endRangeElementPointer.element
        if (startRangeElement == null || endRangeElement == null) return
        val file = descriptor.psiElement.containingFile
        val document = PsiDocumentManager.getInstance(project).getDocument(file)
        document?.deleteString(startRangeElement.textOffset, endRangeElement.textOffset + endRangeElement.textLength)
        if (text == null || document == null) return
        document.insertString(startRangeElement.textOffset, text)
    }
}
