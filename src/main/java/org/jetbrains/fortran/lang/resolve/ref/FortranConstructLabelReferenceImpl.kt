package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranFile
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranConstructLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranConstructLabelImplMixin

class FortranConstructLabelReferenceImpl(element: FortranConstructLabelImplMixin) :
        FortranReferenceBase<FortranConstructLabelImplMixin>(element), FortranReference {

    override val FortranConstructLabelImplMixin.referenceAnchor: PsiElement get() = referenceNameElement

    override fun getVariants(): Array<Any> = emptyArray()

    override fun resolveInner(): List<FortranNamedElement>  {
        var psiElement : PsiElement = element
        // find the root of the tree
        while (psiElement !is FortranFile) psiElement = psiElement.parent

        // find all labels in it
        val tmp = PsiTreeUtil.findChildrenOfType(psiElement, FortranConstructLabelDeclImpl::class.java)
                .filter {element.gelLabelValue() == it.gelLabelValue() }
                .toMutableList()
        return tmp
    }
}