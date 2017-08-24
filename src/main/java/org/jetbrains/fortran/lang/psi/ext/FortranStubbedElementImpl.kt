package org.jetbrains.fortran.lang.psi.ext

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.resolve.ref.FortranReference

abstract class FortranStubbedElementImpl <StubT : StubElement<*>>
    : StubBasedPsiElementBase<StubT>, FortranCompositeElement {

    constructor(node: ASTNode) : super(node)

    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getReference(): FortranReference? = null

    override fun toString(): String = "${javaClass.simpleName}($elementType)"
}