package org.jetbrains.fortran.ide.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.ide.highlighter.FortranHighlightingColors
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORD

class FortranHighlightingAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // Identifiers with keywords
        if (element.node.elementType === IDENTIFIER) {
            holder.createInfoAnnotation(element, null).textAttributes = FortranHighlightingColors.IDENTIFIER.textAttributesKey
        }
        if (element.node.elementType === KEYWORD) {
            holder.createInfoAnnotation(element, null).textAttributes = FortranHighlightingColors.KEYWORD.textAttributesKey
        }



    }
}

