package org.jetbrains.fortran.ide.findUsages

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
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
        return null
    }

    override fun getType(element: PsiElement): String {
        if (element is FortranLabelDecl) {
            return "Fortran numerical label"
        } else if (element is FortranConstructNameDecl) {
            return "Construct name"
        } else {
            return ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        if (element is FortranLabelDecl) {
            return element.text
        } else if (element is FortranConstructNameDeclImplMixin) {
            return element.gelLabelValue()
        } else {
            return ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        if (element is FortranLabelDecl) {
            return element.parent.text
        } else if (element is FortranConstructNameDeclImplMixin) {
            return element.parent.text
        } else {
            return ""
        }
    }
}