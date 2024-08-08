package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranConstructName
import org.jetbrains.fortran.lang.psi.FortranExecutableConstruct
import org.jetbrains.fortran.lang.psi.FortranExitStmt
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.beginConstructStmt
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranConstructNameMismatchInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Construct name mismatch"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitConstructName(name: FortranConstructName) {
                    if (name.parent !is FortranExitStmt) {
                        val construct = name.parent.parent
                        val realName = (construct as FortranExecutableConstruct).beginConstructStmt?.constructNameDecl?.name

                        if (!name.referenceName.equals(realName, true)) {
                            holder.registerProblemForReference(name.reference,
                                    ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                                    "Construct name mismatch",
                                    SubstituteTextFix(name.smartPointer(), realName, "Construct name fix")
                            )
                        }
                    }
                }
            }
}
