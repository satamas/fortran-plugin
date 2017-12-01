package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.TokenType
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranContinueStmt
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranContinueInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Continue statement without label"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitContinueStmt(continueStmt: FortranContinueStmt) {
                    if (continueStmt.labelDecl == null) {
                        val lastElement = if (continueStmt.nextSibling.node.elementType != TokenType.WHITE_SPACE) continueStmt else continueStmt.nextSibling
                        holder.registerProblem(continueStmt,
                                "Continue statement without label",
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                SubstituteTextFix(continueStmt.smartPointer(), lastElement.smartPointer(), "", "Continue statement without label fix")
                        )
                    }
                }
            }
}