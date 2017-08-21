package org.jetbrains.fortran.lang.psi.ext

import org.jetbrains.fortran.lang.psi.FortranDataPath

interface FortranEntitiesOwner {
    val subprograms : Array<FortranNamedElement>

    val variables : Array<FortranNamedElement>

    val unit : FortranNamedElement?

    val usedModules : Array<FortranDataPath>
}