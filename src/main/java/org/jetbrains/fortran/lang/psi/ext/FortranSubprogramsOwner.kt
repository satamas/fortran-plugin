package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement

interface FortranSubprogramsOwner {
    val subprograms : Array<FortranNamedElement>
}