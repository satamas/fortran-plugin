package org.jetbrains.fortran.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import org.jetbrains.fortran.lang.psi.FortranCompositeElement

abstract class FortranCompositeElementImpl(node : ASTNode) : ASTWrapperPsiElement(node), FortranCompositeElement {
  override fun toString() = getNode().getElementType().toString()

  override fun getReference(): PsiReference? = null
}
