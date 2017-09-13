package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranUnitImplMixin

class FortranUnitReferenceImpl(element: FortranUnitImplMixin) :
        FortranReferenceBase<FortranUnitImplMixin>(element), FortranReference {

    override val FortranUnitImplMixin.referenceAnchor: PsiElement get() = integerliteral

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> = emptyList()
}