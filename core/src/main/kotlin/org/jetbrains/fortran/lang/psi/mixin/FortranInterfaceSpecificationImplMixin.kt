package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranInterfaceSpecificationStub

abstract class FortranInterfaceSpecificationImplMixin : FortranStubbedElementImpl<FortranInterfaceSpecificationStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranInterfaceSpecificationStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}