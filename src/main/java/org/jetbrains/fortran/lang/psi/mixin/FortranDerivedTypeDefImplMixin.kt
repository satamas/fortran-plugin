package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.core.stubs.FortranDerivedTypeDefStub
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranDerivedTypeDef
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl

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

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(node.psi, FortranEntityDecl::class.java).toTypedArray()

    override val subprograms: Array<FortranNamedElement>
        get() = emptyArray()

    override val unit : FortranNamedElement? = null

    override val usedModules: Array<FortranDataPath>
        get() = emptyArray()

    override val types: Array<FortranNamedElement>
        get() = emptyArray()
}