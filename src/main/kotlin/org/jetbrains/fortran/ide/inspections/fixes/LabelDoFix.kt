package org.jetbrains.fortran.ide.inspections.fixes

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.SmartPsiElementPointer
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.psi.FortranLabelDoStmt
import org.jetbrains.fortran.lang.psi.FortranLabeledDoConstruct


class LabelDoFix(private val labelDoConstructPointer: SmartPsiElementPointer<PsiElement>) : LocalQuickFix {
    override fun getName() = "Convert to Nonlabaled do construct"
    override fun getFamilyName() = "Convert to Nonlabaled do construct"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val labelDoConstruct = labelDoConstructPointer.element as? FortranLabeledDoConstruct ?: return
        convert(labelDoConstruct, project, descriptor)
    }

    private fun convert(loop : FortranLabeledDoConstruct,  project: Project, descriptor: ProblemDescriptor) {
        // ending of the loop
        val termStmt = loop.doTermActionStmt
        val innerLoop = loop.labeledDoTermConstract
        val file = descriptor.psiElement.containingFile
        val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return
        val indentString = CodeStyleManager.getInstance(file.project)!!.getLineIndent(document, loop.labelDoStmt.textOffset)
        if (loop.endDoStmt != null) {
            // do nothing! label may be deleted by another inspection
        } else if (innerLoop != null) {
            addEndStmt(innerLoop, project, descriptor, indentString, loop.labelDoStmt.constructNameDecl?.name)
            convert(innerLoop, project, descriptor)
        } else if (termStmt != null) {
            addEndStmt(termStmt, project, descriptor, indentString, loop.labelDoStmt.constructNameDecl?.name)
        }
        // beginning of the loop must be processed after the end not to change textranges before we use them
        removeLabelFromDoStmt(loop.labelDoStmt, project, descriptor)
    }

    private fun removeLabelFromDoStmt(stmt: FortranLabelDoStmt, project: Project, descriptor: ProblemDescriptor) {
        val label = stmt.label
        val startRange = label.textOffset
        val endRange = if (label.nextSibling.node.elementType != TokenType.WHITE_SPACE)
            label.textOffset + label.textLength
        else
            label.nextSibling.textOffset + label.nextSibling.textLength
        val file = descriptor.psiElement.containingFile
        val document = PsiDocumentManager.getInstance(project).getDocument(file)
        document?.deleteString(startRange, endRange)
    }

    private fun addEndStmt(lastElement: FortranCompositeElement, project: Project, descriptor: ProblemDescriptor, indentString : String?, constructName: String?) {
        val file = descriptor.psiElement.containingFile
        val document = PsiDocumentManager.getInstance(project).getDocument(file)
        val text = if (constructName != null)
            "\n${indentString}end do $constructName"
        else
            "\n${indentString}end do"
        document?.insertString(lastElement.textOffset + lastElement.textLength, text)

    }
}