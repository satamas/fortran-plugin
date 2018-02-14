package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranUnitDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl

abstract class FortranUnitDeclImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranUnitDecl {
    override fun getNameIdentifier(): PsiElement? = integerliteral

    override fun setName(name: String): PsiElement? {
        return this
    }

    fun getUnitValue() = integerliteral.text.toInt()

}