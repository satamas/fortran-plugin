package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranBlockData
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranBlockDataImplMixin (node : ASTNode) : FortranProgramUnitImpl(node), FortranBlockData {
    override fun getNameIdentifier(): PsiElement? = blockDataStmt.identifier

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .toTypedArray()

    override val subprograms: Array<FortranNamedElement>
        get() = emptyArray()
}