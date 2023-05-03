package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.FortranInterfaceSpecification
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranInterfaceSpecificationStub
import org.jetbrains.fortran.lang.stubs.FortranInternalSubprogramPartStub

abstract class FortranInterfaceSpecificationImplMixin : FortranStubbedElementImpl<FortranInterfaceSpecificationStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranInterfaceSpecificationStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}