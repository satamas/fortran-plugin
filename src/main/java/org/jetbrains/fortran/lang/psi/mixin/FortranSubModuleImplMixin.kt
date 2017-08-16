package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranSubmodule
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranSubModuleImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranSubmodule {
    override fun getNameIdentifier(): PsiElement? = submoduleStmt.entityDecl

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .toTypedArray()

    override val unit: FortranNamedElement
        get() = (submoduleStmt.entityDecl as FortranNamedElement)

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(moduleSubprogramPart, FortranProgramUnit::class.java)
                .toTypedArray()
}
