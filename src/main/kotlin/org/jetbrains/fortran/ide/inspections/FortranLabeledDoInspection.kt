package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.LabelDoFix
import org.jetbrains.fortran.lang.psi.FortranExecutableConstruct
import org.jetbrains.fortran.lang.psi.FortranLabeledDoConstruct
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranLabeledDoInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Labeled do construct is deprecated"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitExecutableConstruct(labelDo: FortranExecutableConstruct) {
                    if (labelDo !is FortranLabeledDoConstruct) return
                    if (labelDo.parent !is FortranLabeledDoConstruct
                            && (labelDo.labeledDoTermConstract != null || labelDo.doTermActionStmt != null
                            || labelDo.endDoStmt != null)) {
                        holder.registerProblem(labelDo.labelDoStmt.label,
                                "Labeled do construct is deprecated",
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                LabelDoFix(labelDo.smartPointer())
                        )
                    }
                }
            }
}