package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranCommonBlockDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranCommonBlockReferenceImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranReference

abstract class FortranCommonBlockDeclImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranCommonBlockDecl {
    override fun getNameIdentifier(): PsiElement? = referenceNameElement

    override fun setName(name: String): PsiElement? {
        return this
    }

    override val referenceNameElement: PsiElement get() = notNullChild(findChildByType(FortranTypes.IDENTIFIER))

    override val referenceName: String get() = referenceNameElement.text

    override fun getReference(): FortranReference = FortranCommonBlockReferenceImpl(this)

}
