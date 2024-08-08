package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranBlockStub

abstract class FortranBlockImplMixin: FortranStubbedElementImpl<FortranBlockStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranBlockStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}