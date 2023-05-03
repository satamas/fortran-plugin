package org.jetbrains.fortran.lang.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl

abstract class FortranNamedElementImpl(node: ASTNode) : FortranCompositeElementImpl(node), FortranNamedElement, PsiNameIdentifierOwner {
    override fun getNameIdentifier(): PsiElement? = findChildByType(FortranTypes.IDENTIFIER)

    override fun getName(): String? = nameIdentifier?.text

    override fun getNavigationElement(): PsiElement = nameIdentifier ?: this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()
}