package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.core.stubs.FortranEntityDeclStub
import org.jetbrains.fortran.lang.psi.*


abstract class FortranTypeDeclMixin : FortranEntityDeclMixin, FortranTypeDecl {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranEntityDeclStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getAssumedShapeSpecList() = emptyList<FortranAssumedShapeSpec>()
    override fun getAssumedSizeSpec() : FortranAssumedSizeSpec? = null
    override fun getCharLength() : FortranCharLength? = null
    override fun getDataStmtValueList() = emptyList<FortranDataStmtValue>()
    override fun getDeferredShapeSpecList() = emptyList<FortranDeferredShapeSpec>()
    override fun getExplicitCoshapeSpec() : FortranExplicitCoshapeSpec ? = null
    override fun getExplicitShapeSpecList() = emptyList<FortranExplicitShapeSpec>()
    override fun getExpr() : FortranExpr? = null
    override fun getImpliedShapeSpecList() = emptyList<FortranImpliedShapeSpec>()
    override fun getEq() : PsiElement? = null
    override fun getLbracket() : PsiElement? = null
    override fun getLpar() : PsiElement? = null
    override fun getMul() : PsiElement? = null
    override fun getPointerAssmnt() : PsiElement? = null
    override fun getRbracket() : PsiElement? = null
    override fun getRpar() : PsiElement? = null
}