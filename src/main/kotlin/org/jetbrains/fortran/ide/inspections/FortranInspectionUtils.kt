package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference


fun registerProblem(
        holder: ProblemsHolder,
        psiElement: PsiElement,
        descriptionTemplate: String,
        highlightType: ProblemHighlightType,
        vararg fixes: LocalQuickFix
) {
    if (psiElement.textOffset >= psiElement.textRange.endOffset) return
    holder.registerProblem(psiElement, descriptionTemplate, highlightType, *fixes)
}

fun registerProblem(holder: ProblemsHolder, psiElement: PsiElement, descriptionTemplate: String) {
    if (psiElement.textOffset >= psiElement.textRange.endOffset) return
    holder.registerProblem(psiElement, descriptionTemplate)
}

fun registerProblemForReference(
        holder: ProblemsHolder,
        reference: PsiReference,
        highlightType: ProblemHighlightType,
        descriptionTemplate: String,
        vararg fixes: LocalQuickFix?
) {
    val psiElement = reference.element
    if (psiElement.textOffset >= psiElement.textRange.endOffset) return
    holder.registerProblemForReference(reference, highlightType, descriptionTemplate, *fixes)
}