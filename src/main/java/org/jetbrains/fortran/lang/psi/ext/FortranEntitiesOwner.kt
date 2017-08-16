package org.jetbrains.fortran.lang.psi.ext

interface FortranEntitiesOwner {
    val subprograms : Array<FortranNamedElement>

    val variables : Array<FortranNamedElement>

    val unit : FortranNamedElement?

    val usedModules : Array<FortranNamedElement>
}