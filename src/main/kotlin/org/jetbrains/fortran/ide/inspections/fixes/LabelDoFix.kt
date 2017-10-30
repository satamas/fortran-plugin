package org.jetbrains.fortran.ide.inspections.fixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.openapi.editor.Document
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranLabelImplMixin
import org.jetbrains.fortran.lang.resolve.FortranLabelReferenceImpl

class LabelDoFix(private val labelDoConstructPointer: SmartPsiElementPointer<PsiElement>) : LocalQuickFix {
    override fun getName() = "Convert to Nonlabaled do construct"
    override fun getFamilyName() = "Convert to Nonlabaled do construct"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val labelDoConstruct = labelDoConstructPointer.element as? FortranLabeledDoConstruct ?: return

        // ending of the loop
        val termStmt = labelDoConstruct.doTermActionStmt
        val innerLoop = labelDoConstruct.labeledDoTermConstract
        val file = descriptor.psiElement.containingFile
        val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return
        val indentString = CodeStyleManager.getInstance(file.project)!!.getLineIndent(document, labelDoConstruct.labelDoStmt.textOffset) ?: ""
        when {
            innerLoop != null -> addEndStmt(innerLoop, document, indentString, labelDoConstruct.labelDoStmt.constructNameDecl?.name)
            termStmt != null -> {
                addEndStmt(termStmt, document, indentString, labelDoConstruct.labelDoStmt.constructNameDecl?.name)
                deleteLabelIfPossible(termStmt.firstChild, document)
            }
            else -> // if we have end do we don't need to add it
                deleteLabelIfPossible(labelDoConstruct.endDoStmt!!.labelDecl, document)
        }
        // beginning of the loop must be processed after the end not to change textranges before we use them
        removeLabelFromDoStmt(labelDoConstruct.labelDoStmt, document)
    }

    private fun removeLabelFromDoStmt(stmt: FortranLabelDoStmt, document : Document) {
        val label = stmt.label
        val startRange = label.textOffset
        val endRange = if (label.nextSibling.node.elementType != TokenType.WHITE_SPACE)
            label.textOffset + label.textLength
        else
            label.nextSibling.textOffset + label.nextSibling.textLength
        document.deleteString(startRange, endRange)
    }

    private fun addEndStmt(lastElement: FortranCompositeElement, document : Document, indentString : String, constructName: String?) {
        val text = if (constructName != null)
            "\n${indentString}end do $constructName"
        else
            "\n${indentString}end do"
        document.insertString(lastElement.textOffset + lastElement.textLength, text)
    }

    private fun deleteLabelIfPossible(label: PsiElement?, document : Document) {
        val labelDecl = label as? FortranLabelDecl ?: return
        val unit = runReadAction { PsiTreeUtil.getParentOfType(labelDecl, FortranProgramUnit::class.java) }
        val results = runReadAction {
            PsiTreeUtil.findChildrenOfType(unit, FortranLabelImpl::class.java)
        }.filter { (label as FortranLabelDeclImpl).getLabelValue() == it.getLabelValue() }
                .map { FortranLabelReferenceImpl(it as FortranLabelImplMixin) }

        if (results.size == 1) {
            val rangeEnd = if (labelDecl.nextSibling.node.elementType != TokenType.WHITE_SPACE)
                labelDecl.textRange.endOffset
            else
                labelDecl.nextSibling.textOffset + labelDecl.nextSibling.textLength
            document.deleteString(labelDecl.textOffset, rangeEnd)
        }
    }
}