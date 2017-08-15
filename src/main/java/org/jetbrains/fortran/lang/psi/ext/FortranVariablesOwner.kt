package org.jetbrains.fortran.lang.psi.ext


interface FortranVariablesOwner {
    val variables : Array<FortranNamedElement>
}