package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranDerivedTypeDef
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranDerivedTypeDefStub

abstract class FortranDerivedTypeDefImplMixin : FortranStubbedNamedElementImpl<FortranDerivedTypeDefStub>, FortranDerivedTypeDef {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranDerivedTypeDefStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun setName(name: String): PsiElement? {
        return this
    }

    override fun getNameIdentifier(): PsiElement? = derivedTypeStmt.typeDecl

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) stub.name else nameIdentifier?.text
    }

    override val variables: List<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(this, FortranEntityDecl::class.java).toList()

    override val subprograms: List<FortranNamedElement>
        get() = emptyList()

    override val unit : FortranNamedElement? = null

    override val usedModules: List<FortranDataPath>
        get() = emptyList()

    override val types: List<FortranNamedElement>
        get() = emptyList()
}