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

        // inside program unit
        val innerNames = programUnit.variables.filter { element.referenceName == it.name }
                .toMutableList()
                .plus(programUnit.subprograms.filter { element.referenceName == it.name }
                .toMutableList())
        if (programUnit.firstChild !is FortranBlock) {
            val decl = (programUnit.firstChild as FortranNameStmt).entityDecl
            if (decl != null && decl.name == element.referenceName) {
                return innerNames.plus(decl)
            }
        }
        return innerNames
    }
}