package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranCommonBlockDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl
import org.jetbrains.fortran.lang.resolve.FortranCommonBlockReferenceImpl
import org.jetbrains.fortran.lang.resolve.FortranReference

abstract class FortranCommonBlockDeclImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranCommonBlockDecl {
    override fun getNameIdentifier(): PsiElement? = referenceNameElement

    override fun setName(name: String): PsiElement? {
        return this
    }

    override val referenceNameElement: PsiElement
        get() {
            return notNullChild<PsiElement>(findChildByType(FortranTypes.IDENTIFIER))
        }

    override val referenceName: String get() = referenceNameElement.text

    override fun getReference(): FortranReference = FortranCommonBlockReferenceImpl(this)

}
