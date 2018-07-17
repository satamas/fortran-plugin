package org.jetbrains.fortran.ide.annotator


import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import org.jetbrains.fortran.ide.highlighter.FortranHighlightingColors
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORDS

class FortranHighlightingAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Identifiers with keywords
        if (element.node.elementType === IDENTIFIER) {
            if (element.parent !is FortranConstructNameDecl) {
                holder.createInfoAnnotation(element, null).textAttributes = FortranHighlightingColors.IDENTIFIER.textAttributesKey
            } else {
                val decl = element.parent
                if (ReferencesSearch.search(decl, GlobalSearchScope.fileScope(decl.containingFile)).findFirst() != null) {
                    holder.createInfoAnnotation(element, null).textAttributes = FortranHighlightingColors.IDENTIFIER.textAttributesKey
                }
            }
        }
        if (KEYWORDS.contains(element.node.elementType)) {
            holder.createInfoAnnotation(element, null).textAttributes = FortranHighlightingColors.KEYWORD.textAttributesKey
        }
    }
}

