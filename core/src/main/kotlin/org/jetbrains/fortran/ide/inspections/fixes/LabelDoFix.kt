package org.jetbrains.fortran.ide.inspections.fixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemDescriptorBase
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.fortran.ide.inspections.FortranContinueInspection
import org.jetbrains.fortran.ide.inspections.FortranUnusedLabelInspection
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.lastChildOfType
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class LabelDoFix(private val labelDoConstructPointer: SmartPsiElementPointer<PsiElement>) : LocalQuickFix {
    override fun getName() = "Convert to Nonlabaled do construct"
    override fun getFamilyName() = "Convert to Nonlabaled do construct"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val labelDoConstruct = labelDoConstructPointer.element as? FortranLabeledDoConstruct ?: return
        val parentPointer = labelDoConstruct.parent.smartPointer()
        val parentPosition = labelDoConstruct.parent.children.indexOf(labelDoConstruct)

        // ending of the loop
        val termStmt = labelDoConstruct.doTermActionStmt
        val innerLoop = labelDoConstruct.labeledDoTermConstract

        val file = descriptor.psiElement.containingFile
        val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return

        val indentString = if (file is FortranFile)
            CodeStyleManager.getInstance(file.project)!!.getLineIndent(document, labelDoConstruct.labelDoStmt.textOffset) ?: ""
        else
            "      "

        var termStmtWasUsed = false
        when {
            innerLoop != null -> addEndStmt(innerLoop, document, indentString, labelDoConstruct.labelDoStmt.constructNameDecl?.name)
            termStmt != null -> {
                addEndStmt(termStmt, document, indentString, labelDoConstruct.labelDoStmt.constructNameDecl?.name)
                termStmtWasUsed = true
            }
        }
        // beginning of the loop must be processed after the end not to change textranges before we use them
        removeLabelFromDoStmt(labelDoConstruct.labelDoStmt, document)
        PsiDocumentManager.getInstance(project).commitDocument(document)

        deleteLabelsAndContinueStmt(parentPointer, parentPosition, termStmtWasUsed, project, document)
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

    private fun deleteLabelsAndContinueStmt(parentPointer : SmartPsiElementPointer<PsiElement>,
                                            parentPosition : Int,
                                            termStmtWasUsed : Boolean,
                                            project: Project,
                                            document: Document) {
        // our loop is nonlabel do construct now. Need to have new pointer instead of an invalidated one
        val nonlabeledDoConstruct = parentPointer.element?.children?.get(parentPosition) as FortranNonlabelDoConstruct
        // we need a new descriptor to access file in the substitute text fix
        val newDescriptor = ProblemDescriptorBase(nonlabeledDoConstruct, nonlabeledDoConstruct, "",
                emptyArray(), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, false, null,
                true, false
        )
        if (termStmtWasUsed) {
            val lastStmt = nonlabeledDoConstruct.block?.lastChildOfType(FortranStmt::class)
            deleteLabelIfPossible(lastStmt?.firstChild, project, newDescriptor)
            if (lastStmt is FortranContinueStmt) {
                val continuePointer = lastStmt.lastChild.smartPointer()
                PsiDocumentManager.getInstance(project).commitDocument(document)
                val continueStmt = continuePointer.element?.parent as? FortranContinueStmt ?: return
                if (continueStmt.labelDecl == null) {
                    FortranContinueInspection.createFix(continueStmt).applyFix(project, newDescriptor)
                }
            }
        } else {
            deleteLabelIfPossible(nonlabeledDoConstruct.endDoStmt?.labelDecl, project, newDescriptor)
        }
    }

    private fun deleteLabelIfPossible(label: PsiElement?, project: Project, descriptor: ProblemDescriptor) {
        if (label is FortranLabelDecl && FortranUnusedLabelInspection.isUnusedLabel(label)) {
            FortranUnusedLabelInspection.createFix(label).applyFix(project, descriptor)
        }
    }
}