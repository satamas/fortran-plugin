package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.stubs.FortranInterfaceBodyStub
import org.jetbrains.fortran.lang.psi.FortranInterfaceBody
import org.jetbrains.fortran.lang.psi.FortranNameStmt
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl

abstract class FortranInterfaceBodyImplMixin : FortranStubbedNamedElementImpl<FortranInterfaceBodyStub>,         FortranNamedElement, FortranInterfaceBody {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranInterfaceBodyStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = (firstChild as FortranNameStmt).entityDecl

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) stub.name else nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement? {
        return this
    }
}