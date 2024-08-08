package org.jetbrains.fortran.lang.psi

interface FortranBeginUnitStmt : FortranStmt {
    val entityDecl: FortranEntityDecl?
}
