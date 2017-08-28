package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.core.stubs.FortranEntityDeclStub
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl

abstract class FortranEntityDeclMixin : FortranStubbedNamedElementImpl<FortranEntityDeclStub>, FortranEntityDecl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranEntityDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = findChildByType(FortranTypes.IDENTIFIER)

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) stub.name else nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement? {
        return this
    }
}