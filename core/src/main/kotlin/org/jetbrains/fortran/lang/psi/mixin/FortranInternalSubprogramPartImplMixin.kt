package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranInternalSubprogramPartStub

abstract class FortranInternalSubprogramPartImplMixin: FortranStubbedElementImpl<FortranInternalSubprogramPartStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranInternalSubprogramPartStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}