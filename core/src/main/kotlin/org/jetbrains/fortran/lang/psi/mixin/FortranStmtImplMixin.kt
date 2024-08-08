package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.FortranStmt
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranStatementStub

abstract class FortranStmtImplMixin : FortranStubbedElementImpl<FortranStatementStub>, FortranStmt {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranStatementStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}