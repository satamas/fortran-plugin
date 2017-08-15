package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranFunctionSubprogram
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranFunctionSubprogramImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranNamedElement, FortranFunctionSubprogram {
    override fun getNameIdentifier(): PsiElement? = functionStmt.identifier

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .toTypedArray()

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranProgramUnit::class.java)
                .toTypedArray()
}