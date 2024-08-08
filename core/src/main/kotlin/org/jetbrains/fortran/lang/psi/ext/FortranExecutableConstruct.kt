package org.jetbrains.fortran.lang.psi.ext

import org.jetbrains.fortran.lang.psi.*

val FortranExecutableConstruct.beginConstructStmt: FortranBeginConstructStmt? get() {
    return when (this) {
        is FortranAssociateConstruct -> this.associateStmt
        is FortranBlockConstruct -> this.blockStmt
        is FortranCaseConstruct -> this.selectCaseStmt
        is FortranCriticalConstruct -> this.criticalStmt
        is FortranNonlabelDoConstruct -> this.nonlabelDoStmt
        is FortranForallConstruct -> this.forallConstructStmt
        is FortranIfConstruct -> this.ifThenStmt
        is FortranSelectTypeConstruct -> this.selectTypeStmt
        is FortranWhereConstruct -> this.whereConstructStmt
        is FortranLabeledDoConstruct -> this.labelDoStmt
        else -> null
    }
}

val FortranExecutableConstruct.endConstructStmt: FortranStmt? get() {
    return when (this) {
        is FortranAssociateConstruct -> this.endAssociateStmt
        is FortranBlockConstruct -> this.endBlockStmt
        is FortranCaseConstruct -> this.endSelectStmt
        is FortranCriticalConstruct -> this.endCriticalStmt
        is FortranNonlabelDoConstruct -> this.endDoStmt
        is FortranForallConstruct -> this.endForallStmt
        is FortranIfConstruct -> this.endIfStmt
        is FortranSelectTypeConstruct -> this.selectTypeStmt
        is FortranWhereConstruct -> this.endWhereStmt
        is FortranLabeledDoConstruct -> this.endDoStmt
        else -> null
    }
}

val FortranExecutableConstruct.constructType: String? get() {
    return when (this) {
        is FortranAssociateConstruct -> "associate"
        is FortranBlockConstruct -> "block"
        is FortranCaseConstruct -> "select"
        is FortranCriticalConstruct -> "critical"
        is FortranNonlabelDoConstruct -> "do"
        is FortranForallConstruct -> "forall"
        is FortranIfConstruct -> "if"
        is FortranSelectTypeConstruct -> "select"
        is FortranWhereConstruct -> "where"
        is FortranLabeledDoConstruct -> "do"
        else -> null
    }
}
