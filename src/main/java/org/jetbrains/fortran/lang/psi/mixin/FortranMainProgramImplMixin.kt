package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranMainProgram
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranMainProgramImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranMainProgram {
    override fun getNameIdentifier(): PsiElement? = programStmt?.identifier

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .toTypedArray()

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranProgramUnit::class.java)
                .toTypedArray()
}