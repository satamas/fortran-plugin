package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import org.jetbrains.fortran.lang.psi.FortranCompositeElement

interface FortranReference : PsiPolyVariantReference {
    override fun getElement(): FortranCompositeElement

    override fun resolve(): PsiElement? //FortranCompositeElement?

    fun multiResolve(): List<FortranCompositeElement>
}