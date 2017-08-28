package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.core.stubs.FortranDataPathStub
import org.jetbrains.fortran.lang.psi.FortranDataPath

abstract class FortranTypeNameImplMixin : FortranDataPathImplMixin, FortranDataPath {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranDataPathStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}