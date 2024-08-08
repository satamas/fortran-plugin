package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranOnlyStmtStub

abstract class FortranOnlyStmtImplMixin : FortranStubbedElementImpl<FortranOnlyStmtStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranOnlyStmtStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}