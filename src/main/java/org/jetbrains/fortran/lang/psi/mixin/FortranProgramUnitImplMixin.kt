package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl

abstract class FortranProgramUnitImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranProgramUnit {
    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: Array<FortranNamedElement>
        get() = emptyArray()

    override val subprograms: Array<FortranNamedElement>
        get() = emptyArray()
}