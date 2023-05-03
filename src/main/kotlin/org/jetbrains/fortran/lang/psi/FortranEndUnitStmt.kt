package org.jetbrains.fortran.lang.psi

interface FortranEndUnitStmt : FortranStmt {
    val dataPath: FortranDataPath?
        get() = null
}
