package org.jetbrains.fortran.lang.psi.ext

import org.jetbrains.fortran.lang.psi.*

val FortranExecutableConstruct.beginConstructStmt: FortranStmt? get() {
    return when (this) {
        is FortranBlockConstruct -> this.blockStmt
        is FortranCriticalConstruct -> this.criticalStmt
        is FortranAssociateConstruct -> this.associateStmt
        is FortranForallConstruct -> this.forallConstructStmt
        else -> null
    }
}

val FortranExecutableConstruct.endConstructStmt: FortranStmt? get() {
    return when (this) {
        is FortranBlockConstruct -> this.endBlockStmt
        is FortranCriticalConstruct -> this.endCriticalStmt
        is FortranAssociateConstruct -> this.endAssociateStmt
        is FortranForallConstruct -> this.endForallStmt
        else -> null
    }
}
