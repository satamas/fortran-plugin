package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranLabel
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl
import org.jetbrains.fortran.lang.resolve.FortranReference
import org.jetbrains.fortran.lang.resolve.FortranUnitReferenceImpl

abstract class FortranUnitImplMixin(node : ASTNode) : FortranCompositeElementImpl(node), FortranLabel {
        override val referenceNameElement: PsiElement get() = integerliteral

        override val referenceName: String get() = referenceNameElement.text

        override fun getReference(): FortranReference = FortranUnitReferenceImpl(this)

        fun getUnitValue() = integerliteral.text.toInt()
}