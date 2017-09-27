package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.SmartPointerManager
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.beginConstructStmt

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
                                    SubstituteTextFix(SmartPointerManager.getInstance(name.project).createSmartPsiElementPointer(name),
                                            SmartPointerManager.getInstance(name.project).createSmartPsiElementPointer(name), realName, "Construct name fix")
                            )
                        }
                    }
                }
            }
}
