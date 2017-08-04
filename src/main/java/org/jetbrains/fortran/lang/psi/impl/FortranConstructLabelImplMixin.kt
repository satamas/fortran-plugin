package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranConstructLabel
import org.jetbrains.fortran.lang.resolve.ref.FortranConstructLabelReferenceImpl
import org.jetbrains.fortran.lang.resolve.ref.FortranReference


abstract class FortranConstructLabelImplMixin(node : ASTNode) : FortranCompositeElementImpl(node), FortranConstructLabel {
    override val referenceNameElement: PsiElement get() = notNullChild(findChildByType(FortranTypes.IDENTIFIER))

    override val referenceName: String get() = referenceNameElement.text

    override fun getReference(): FortranReference = FortranConstructLabelReferenceImpl(this)

    fun gelLabelValue() = referenceNameElement.text
}