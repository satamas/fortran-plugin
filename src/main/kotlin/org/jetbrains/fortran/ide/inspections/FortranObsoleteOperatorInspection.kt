package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranRelExpr
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer
import java.util.*

class FortranObsoleteOperatorInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Obsolete operator"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitRelExpr(expr: FortranRelExpr) {

                    val operator = expr.relOp

                    val newOperatorText = when (operator.text.lowercase(Locale.getDefault())){
                        ".eq." -> "=="
                        ".ne." -> "/="
                        ".lt." -> "<"
                        ".gt." -> ">"
                        ".le." -> "<="
                        ".ge." -> ">="
                        else -> null
                    }
                    if (newOperatorText != null) {
                        registerProblem(
                                holder,
                                operator,
                                "Obsolete operator",
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                SubstituteTextFix(operator.smartPointer(), newOperatorText, "Obsolete operator fix")
                        )
                    }
                }
            }
}