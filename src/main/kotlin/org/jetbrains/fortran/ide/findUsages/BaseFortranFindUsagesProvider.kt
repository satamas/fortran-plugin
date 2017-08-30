package org.jetbrains.fortran.ide.findUsages

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.FortranTypeDecl
import org.jetbrains.fortran.lang.psi.mixin.FortranConstructNameDeclImplMixin


abstract class BaseFortranFindUsagesProvider : FindUsagesProvider {
    protected abstract val isFixedFormFortran: Boolean

    override fun getWordsScanner(): WordsScanner? = null

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is FortranLabelDecl
                || psiElement is FortranConstructNameDecl
                || psiElement is FortranEntityDecl
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return "I'm your help ID"
    }

    override fun getType(element: PsiElement): String =
            when (element) {
                is FortranLabelDecl -> "Fortran label"
                is FortranConstructNameDecl -> "Construct name"
                is FortranTypeDecl -> "Type"
                is FortranEntityDecl -> "Entity"
                else -> ""
            }

    override fun getDescriptiveName(element: PsiElement): String =
            when (element) {
                is FortranLabelDecl -> element.text
                is FortranConstructNameDeclImplMixin -> element.getLabelValue()
                is FortranTypeDecl -> element.text
                is FortranEntityDecl -> element.text
                else -> ""
            }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
            when (element) {
                is FortranLabelDecl -> element.parent.text
                is FortranConstructNameDeclImplMixin -> element.parent.text
                is FortranTypeDecl -> element.parent.text
                is FortranEntityDecl -> element.parent.text
                else -> ""
            }
}