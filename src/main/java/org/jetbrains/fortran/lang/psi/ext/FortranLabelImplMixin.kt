package org.jetbrains.fortran.lang.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranLabel
import org.jetbrains.fortran.lang.psi.FortranPsiFactory

abstract class FortranLabelImplMixin(node: ASTNode) : FortranNamedElementImpl(node), FortranLabel {
    override fun getNameIdentifier() = integerLiteral

    override fun getName(): String = nameIdentifier.text

    override fun setName(name: String): PsiElement {
        nameIdentifier.replace(FortranPsiFactory(project).createLabel(name))
        return this
    }
}