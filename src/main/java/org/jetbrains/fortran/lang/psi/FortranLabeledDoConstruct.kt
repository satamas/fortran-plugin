package org.jetbrains.fortran.lang.psi

interface FortranLabeledDoConstruct : FortranExecutableConstruct {
    val exprList: List<FortranExpr?>
    val labelDoStmt: FortranLabelDoStmt
    val doTermActionStmt: FortranStmt?
    val labeledDoTermConstract: FortranLabeledDoConstruct?
    val endDoStmt: FortranEndDoStmt?
}
