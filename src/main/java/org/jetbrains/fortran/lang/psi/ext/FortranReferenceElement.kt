package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.resolve.ref.FortranReference

interface FortranReferenceElement : FortranCompositeElement {
    val referenceNameElement: PsiElement

    val referenceName: String

    override fun getReference(): FortranReference
}