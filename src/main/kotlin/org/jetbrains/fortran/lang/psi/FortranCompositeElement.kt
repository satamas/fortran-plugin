package org.jetbrains.fortran.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

interface FortranCompositeElement : PsiElement {
    override fun getReference(): PsiReference?
}
