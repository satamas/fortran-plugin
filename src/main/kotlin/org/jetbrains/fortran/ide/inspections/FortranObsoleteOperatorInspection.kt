package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranObsoleteOperatorInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Obsolete operator"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitRelExpr(expr: FortranRelExpr) {

                    val operator = expr.relOp

                    val newOperatorText = when (operator.text.toLowerCase()){
                        ".eq." -> "=="
                        ".ne." -> "/="
                        ".lt." -> "<"
                        ".gt." -> ">"
                        ".le." -> "<="
                        ".ge." -> ">="
                        else -> null
                    }
                    if (newOperatorText != null) {
                        holder.registerProblem(operator,
                                "Obsolete operator",
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                SubstituteTextFix(operator.smartPointer(), newOperatorText, "Obsolete operator fix")
                        )
                    }
                }
            }
}