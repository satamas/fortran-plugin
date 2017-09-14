package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.lang.psi.FortranLabel
import org.jetbrains.fortran.lang.psi.FortranVisitor

class FortranUnresolvedLabelInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Unresolved label"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitLabel(label: FortranLabel) {
                    if (label.reference.multiResolve().isEmpty()) {
                        holder.registerProblemForReference(label.reference, ProblemHighlightType.LIKE_UNKNOWN_SYMBOL, "Unresolved label")
                    }
                }
            }
}