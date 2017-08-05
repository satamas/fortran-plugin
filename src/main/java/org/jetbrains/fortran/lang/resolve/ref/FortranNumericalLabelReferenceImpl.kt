package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranNumericalLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranNumericalLabelImplMixin


class FortranNumericalLabelReferenceImpl(element: FortranNumericalLabelImplMixin) :
        FortranReferenceBase<FortranNumericalLabelImplMixin>(element), FortranReference {

    override val FortranNumericalLabelImplMixin.referenceAnchor: PsiElement get() = integerliteral

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        return PsiTreeUtil.findChildrenOfType(programUnit, FortranNumericalLabelDeclImpl::class.java)
                .filter { element.gelLabelValue() == it.gelLabelValue() }
                .toMutableList()
    }
}