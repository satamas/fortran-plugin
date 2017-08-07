package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranConstructLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranConstructLabelImplMixin

class FortranConstructLabelReferenceImpl(element: FortranConstructLabelImplMixin) :
        FortranReferenceBase<FortranConstructLabelImplMixin>(element), FortranReference {

    override val FortranConstructLabelImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement>  {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        // find all labels in it
        return PsiTreeUtil.findChildrenOfType(programUnit, FortranConstructLabelDeclImpl::class.java)
                .filter {element.gelLabelValue() == it.gelLabelValue() }
    }
}