package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranCommonBlockDeclImplMixin

class FortranCommonBlockReferenceImpl(element: FortranCommonBlockDeclImplMixin) :
        FortranReferenceBase<FortranCommonBlockDeclImplMixin>(element), FortranReference {

    override val FortranCommonBlockDeclImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> = listOf(this.element)

    override fun isReferenceTo(element: PsiElement?): Boolean
            = (element is FortranCommonBlockDeclImplMixin && (element.name?.equals(this.element.name, true) ?: false))
}