package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNamedElement
import org.jetbrains.fortran.lang.psi.FortranCompositeElement

interface FortranNamedElement : FortranCompositeElement, NavigatablePsiElement, PsiNamedElement {

}