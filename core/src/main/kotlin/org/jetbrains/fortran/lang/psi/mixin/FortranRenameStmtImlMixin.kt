package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranRenameStmtStub

abstract class FortranRenameStmtImlMixin: FortranStubbedElementImpl<FortranRenameStmtStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranRenameStmtStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}