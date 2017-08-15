package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl

abstract class FortranEntityDeclMixin (node : ASTNode) : FortranNamedElementImpl(node), FortranEntityDecl {
    override fun getNameIdentifier(): PsiElement? = findChildByType(FortranTypes.IDENTIFIER)

    override fun setName(name: String): PsiElement? {
        return this
    }
}