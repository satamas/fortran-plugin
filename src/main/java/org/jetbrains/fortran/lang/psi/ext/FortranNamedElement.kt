package org.jetbrains.fortran.lang.psi.ext

import com.intellij.lang.ASTNode
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl

interface FortranNamedElement : FortranCompositeElement, PsiNameIdentifierOwner, NavigatablePsiElement

abstract class FortranNamedElementImpl(node: ASTNode) : FortranNamedElement, FortranCompositeElementImpl(node)