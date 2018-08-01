package org.jetbrains.fortran.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranUnitDeclImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranUnitImplMixin

class FortranUnitReferenceImpl(element: FortranUnitImplMixin) :
        FortranReferenceBase<FortranUnitImplMixin>(element), FortranReference {

    override val FortranUnitImplMixin.referenceAnchor: PsiElement get() = integerliteral

    override fun resolveInner(incompleteCode: Boolean): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        val unitDeclarations = PsiTreeUtil.findChildrenOfType(programUnit, FortranUnitDeclImpl::class.java)
        return if (incompleteCode) {
            unitDeclarations.toList()
        } else {
            unitDeclarations.filter { element.getUnitValue() == it.getUnitValue() }
        }
    }
}