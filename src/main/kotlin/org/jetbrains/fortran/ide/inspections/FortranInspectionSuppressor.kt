package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement

class FortranInspectionSuppressor : InspectionSuppressor {
    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<out SuppressQuickFix> = emptyArray()

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean = false
}

