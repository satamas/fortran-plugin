package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranNumericalLabel
import org.jetbrains.fortran.lang.resolve.ref.FortranNumericalLabelReferenceImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranReference

abstract class FortranNumericalLabelImplMixin(node : ASTNode) : FortranCompositeElementImpl(node), FortranNumericalLabel {
        override val referenceNameElement: PsiElement get() = integerliteral

        override val referenceName: String get() = referenceNameElement.text

        override fun getReference(): FortranReference = FortranNumericalLabelReferenceImpl(this)

        fun gelLabelValue() = integerliteral.text.toInt()
}

