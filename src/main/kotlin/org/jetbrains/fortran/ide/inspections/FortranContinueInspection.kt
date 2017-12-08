package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranContinueStmt
import org.jetbrains.fortran.lang.psi.FortranTokenType
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranContinueInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Continue statement without label"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitContinueStmt(continueStmt: FortranContinueStmt) {
                    if (continueStmt.labelDecl == null) {
                        // check, if continue stmt is the only nonwhitespase thing on the line
                        val firstStmt = firstStatementOnTheLine(continueStmt)
                        val lastStmt = lastStatementOnTheLine(continueStmt)

                        if (firstStmt != null && lastStmt != null) {
                            holder.registerProblem(continueStmt,
                                    "Continue statement without label",
                                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                                    SubstituteTextFix(firstStmt.smartPointer(), lastStmt.smartPointer(), "", "Continue statement without label fix")
                            )
                        } else {
                            val lastElement = if (continueStmt.nextSibling?.node?.elementType != TokenType.WHITE_SPACE) continueStmt else continueStmt.nextSibling
                            holder.registerProblem(continueStmt,
                                    "Continue statement without label",
                                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                                    SubstituteTextFix(continueStmt.smartPointer(), lastElement.smartPointer(), "", "Continue statement without label fix")
                            )
                        }
                    }
                }
            }


    private fun firstStatementOnTheLine(continueStmt: FortranContinueStmt) : PsiElement? {
        var cor : PsiElement? = continueStmt
        var prev =continueStmt.prevSibling

        while (prev?.node?.elementType == TokenType.WHITE_SPACE || prev?.node?.elementType == FortranTokenType.FIRST_WHITE_SPACE) {
            if (prev?.text?.contains("\n") == true) return cor
            cor = prev
            prev = cor.prevSibling
        }

        return if (prev?.node?.elementType == FortranTypes.EOL)
            cor
        else
            null
    }


    private fun lastStatementOnTheLine(continueStmt: FortranContinueStmt) : PsiElement? {
        var next = continueStmt.nextSibling
        while (next?.node?.elementType == TokenType.WHITE_SPACE) {
            if (next?.text?.contains("\n") == true) return next
            next = next.nextSibling
        }

        return if (next?.node?.elementType == FortranTypes.EOL)
            next
        else
            null
    }
}