package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImplMixin


class FortranLabelReferenceImpl(element: FortranLabelImplMixin) :
        FortranReferenceBase<FortranLabelImplMixin>(element), FortranReference {

    override val FortranLabelImplMixin.referenceAnchor: PsiElement get() = integerliteral

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        return PsiTreeUtil.findChildrenOfType(programUnit, FortranLabelDeclImpl::class.java)
                .filter { element.gelLabelValue() == it.gelLabelValue() }
                .toMutableList()
    }
}