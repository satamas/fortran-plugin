package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranConstructLabelDecl
import org.jetbrains.fortran.lang.psi.FortranNumericalLabelDecl
import org.jetbrains.fortran.lang.psi.FortranTokenType
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranConstructLabelDeclImplMixin


class FortranFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(FortranLexer(false),
                FortranTokenType.WORD_OR_ILITERAL, FortranTokenType.COMMENTS, TokenSet.EMPTY)
    }
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is FortranNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getType(element: PsiElement): String {
        if (element is FortranNumericalLabelDecl) {
            return "Fortran numerical label"
        } else if (element is FortranConstructLabelDecl) {
            return "Constract name"
        } else {
            return ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        if (element is FortranNumericalLabelDecl) {
            return element.text
        } else if (element is FortranConstructLabelDeclImplMixin) {
            return element.gelLabelValue()
        }else {
            return ""
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        if (element is FortranNumericalLabelDecl) {
            return element.parent.text
        } else if (element is FortranConstructLabelDeclImplMixin) {
            return element.parent.text
        }else {
            return ""
        }
    }
}