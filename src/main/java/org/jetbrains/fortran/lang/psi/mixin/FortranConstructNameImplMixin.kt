package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranConstructName
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl
import org.jetbrains.fortran.lang.resolve.FortranConstructNameReferenceImpl
import org.jetbrains.fortran.lang.resolve.FortranReference


abstract class FortranConstructNameImplMixin(node : ASTNode) : FortranCompositeElementImpl(node), FortranConstructName {
    override val referenceNameElement: PsiElement get() = notNullChild(findChildByType(FortranTypes.IDENTIFIER))

    override val referenceName: String get() = referenceNameElement.text

    override fun getReference(): FortranReference = FortranConstructNameReferenceImpl(this)

    fun gelLabelValue() : String = referenceNameElement.text
}