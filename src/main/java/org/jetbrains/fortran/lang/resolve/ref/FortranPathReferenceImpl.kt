package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.mixin.FortranDataPathImplMixin


class FortranPathReferenceImpl(element: FortranDataPathImplMixin) :
        FortranReferenceBase<FortranDataPathImplMixin>(element), FortranReference {

    override val FortranDataPathImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement> {
        val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return emptyList()
        val outerProgramUnit : PsiElement
        // local variables
        val names = programUnit.variables.filter { element.referenceName == it.name }
                .toMutableList()
        if (element.referenceName == programUnit.unit?.name ) names.add(programUnit.unit as FortranNamedElement)
        // if we are real program unit
        if (programUnit.parent !is FortranModuleSubprogramPart
            && programUnit.parent !is FortranInternalSubprogramPart) {
            names.addAll(programUnit.subprograms.filter { element.referenceName == it.name }
                    .toMutableList())

            outerProgramUnit = programUnit
        } else {
            outerProgramUnit = PsiTreeUtil.getParentOfType(programUnit, FortranProgramUnit::class.java) ?: return emptyList()
            names.addAll(outerProgramUnit.variables.filter { element.referenceName == it.name }
                    .toMutableList())
            names.addAll(outerProgramUnit.subprograms.filter { element.referenceName == it.name }
                            .toMutableList())
        }
        // modules

        return names
    }
}