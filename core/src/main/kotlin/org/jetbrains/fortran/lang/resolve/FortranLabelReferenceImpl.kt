package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranLabelImplMixin


class FortranLabelReferenceImpl(element: FortranLabelImplMixin) :
        FortranReferenceBase<FortranLabelImplMixin>(element), FortranReference {

    override val FortranLabelImplMixin.referenceAnchor: PsiElement get() = integerliteral

    override fun resolveInner(incompleteCode: Boolean): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        val labelDeclarations = PsiTreeUtil.findChildrenOfType(programUnit, FortranLabelDeclImpl::class.java)
        return if(incompleteCode){
            labelDeclarations.toList()
        } else{
            labelDeclarations.filter { element.getLabelValue() == it.getLabelValue() }
        }
    }
}