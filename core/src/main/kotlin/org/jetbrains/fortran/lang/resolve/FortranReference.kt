package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement

interface FortranReference : PsiPolyVariantReference {
    override fun getElement(): FortranCompositeElement

    override fun resolve(): PsiElement?

    fun multiResolve(): List<FortranNamedElement>
}