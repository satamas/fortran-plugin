package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranUnitDeclImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranUnitImplMixin

class FortranUnitReferenceImpl(element: FortranUnitImplMixin) :
        FortranReferenceBase<FortranUnitImplMixin>(element), FortranReference {

    override val FortranUnitImplMixin.referenceAnchor: PsiElement get() = integerliteral

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        return PsiTreeUtil.findChildrenOfType(programUnit, FortranUnitDeclImpl::class.java)
                .filter { element.getUnitValue() == it.getUnitValue() }
                .toMutableList()
    }
}