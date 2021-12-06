package org.jetbrains.fortran.ide.annotator


import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import org.jetbrains.fortran.ide.highlighter.FortranHighlightingColors
import org.jetbrains.fortran.lang.FortranTypes.IDENTIFIER
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranTokenSets
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element.node.elementType) {
            IDENTIFIER -> {
                if (element.parent !is FortranConstructNameDecl) {
                    holder
                        .newAnnotation(HighlightSeverity.INFORMATION, "")
                        .range(element)
                        .textAttributes(FortranHighlightingColors.IDENTIFIER.textAttributesKey)
                        .create()
                } else {
                    val decl = element.parent
                    if (ReferencesSearch.search(decl, GlobalSearchScope.fileScope(decl.containingFile)).findFirst() != null) {
                        holder
                            .newAnnotation(HighlightSeverity.INFORMATION, "")
                            .range(element)
                            .textAttributes(FortranHighlightingColors.IDENTIFIER.textAttributesKey)
                            .create()
                    }
                }
            }
            in FortranTokenSets.KEYWORDS -> {
                holder
                    .newAnnotation(HighlightSeverity.INFORMATION, "")
                    .range(element)
                    .textAttributes(FortranHighlightingColors.KEYWORD.textAttributesKey)
                    .create()
            }
            FortranTokenType.CONDITIONALLY_NON_COMPILED_COMMENT -> {
                holder
                    .newAnnotation(HighlightSeverity.INFORMATION, "")
                    .range(element)
                    .textAttributes(FortranHighlightingColors.CONDITIONALLY_NOT_COMPILED.textAttributesKey)
                    .create()
            }
        }
    }
}

