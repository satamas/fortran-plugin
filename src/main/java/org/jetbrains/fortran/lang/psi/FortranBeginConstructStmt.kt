package org.jetbrains.fortran.lang.psi

interface FortranBeginConstructStmt : FortranStmt {
    val constructNameDecl: FortranConstructNameDecl?
}
