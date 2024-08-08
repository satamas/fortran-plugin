package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameDeclImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranConstructNameImplMixin

class FortranConstructNameReferenceImpl(element: FortranConstructNameImplMixin) :
        FortranReferenceBase<FortranConstructNameImplMixin>(element), FortranReference {

    override val FortranConstructNameImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun resolveInner(incompleteCode: Boolean): List<FortranNamedElement>  {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        val constructNames = PsiTreeUtil.findChildrenOfType(programUnit, FortranConstructNameDeclImpl::class.java)

        // find all labels in it
        return if(incompleteCode) {
            constructNames.toList()
        } else{
            constructNames.filter {element.getLabelValue().equals(it.getLabelValue(), true) }
        }
    }
}