package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranMainProgram
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranDataPathImplMixin


class FortranPathReferenceImpl(element: FortranDataPathImplMixin) :
        FortranReferenceBase<FortranDataPathImplMixin>(element), FortranReference {

    override val FortranDataPathImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()

        if (programUnit is FortranMainProgram) {
            return programUnit.variables.filter { element.referenceName == it.name }
                    .toMutableList().plus(programUnit.subprograms.filter { element.referenceName == it.name }
                    .toMutableList())
        }
        return PsiTreeUtil.findChildrenOfType(programUnit, FortranNamedElement::class.java)
                .filter { element.referenceName == it.name }
                .toMutableList()
    }
}