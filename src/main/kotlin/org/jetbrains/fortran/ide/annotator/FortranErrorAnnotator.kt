package org.jetbrains.fortran.ide.annotator

import com.intellij.codeInsight.daemon.impl.HighlightRangeExtension
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl

class FortranErrorAnnotator : Annotator, HighlightRangeExtension {
    override fun isForceHighlightParents(file: PsiFile): Boolean = file is FortranFile || file is FortranFixedFormFile

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val visitor = object : FortranVisitor() {
            override fun visitLabelDecl(labelDecl: FortranLabelDecl) {
                val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return
                val declarations = PsiTreeUtil.findChildrenOfType(programUnit, FortranLabelDeclImpl::class.java)
                    .filter { (labelDecl as FortranLabelDeclImpl).getLabelValue() == it.getLabelValue() }
                if (declarations.size > 1) {
                    holder
                        .newAnnotation(
                            HighlightSeverity.ERROR,
                            "Duplicated label `${(labelDecl as FortranLabelDeclImpl).getLabelValue()}` declaration"
                        )
                        .range(labelDecl)
                        .create()
                }
            }

            override fun visitConstructNameDecl(decl: FortranConstructNameDecl) {
                val programUnit = PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) ?: return
                val declarations = PsiTreeUtil.findChildrenOfType(programUnit, FortranConstructNameDeclImpl::class.java)
                    .filter { decl.name.equals(it.name, true) }
                if (declarations.size > 1) {
                    holder
                        .newAnnotation(HighlightSeverity.ERROR, "Duplicated construct name `${decl.name}` declaration")
                        .range(decl)
                        .create()
                }
            }
        }
        element.accept(visitor)
    }

}