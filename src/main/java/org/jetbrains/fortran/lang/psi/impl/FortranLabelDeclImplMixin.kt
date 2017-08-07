package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl

abstract class FortranLabelDeclImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranLabelDecl {
    override fun getNameIdentifier(): PsiElement? = integerliteral

    override fun setName(name: String): PsiElement? {
        return this
    }

    fun gelLabelValue() = integerliteral.text.toInt()
}
