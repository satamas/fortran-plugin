package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranDataPath

interface FortranEntitiesOwner : PsiElement {
    val subprograms : List<FortranNamedElement>

    val variables : List<FortranNamedElement>

    val unit : FortranNamedElement?

    val usedModules : List<FortranDataPath>

    val types : List<FortranNamedElement>
}