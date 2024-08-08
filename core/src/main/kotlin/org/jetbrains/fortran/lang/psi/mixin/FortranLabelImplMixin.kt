package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranLabel
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl
import org.jetbrains.fortran.lang.resolve.FortranLabelReferenceImpl
import org.jetbrains.fortran.lang.resolve.FortranReference

abstract class FortranLabelImplMixin(node : ASTNode) : FortranCompositeElementImpl(node), FortranLabel {
        override val referenceNameElement: PsiElement get() = integerliteral

        override val referenceName: String get() = referenceNameElement.text

        override fun getReference(): FortranReference = FortranLabelReferenceImpl(this)

        fun getLabelValue() = integerliteral.text.toInt()
}

