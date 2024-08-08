package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranModuleSubprogramPartStub

abstract class FortranModuleSubprogramPartImplMixin: FortranStubbedElementImpl<FortranModuleSubprogramPartStub> {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranModuleSubprogramPartStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
}