package org.jetbrains.fortran.lang.psi.ext

import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranDataPath

interface FortranEntitiesOwner : PsiElement {
    val subprograms : Array<FortranNamedElement>

    val variables : Array<FortranNamedElement>

    val unit : FortranNamedElement?

    val usedModules : Array<FortranDataPath>

    val types : Array<FortranNamedElement>
}