package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.beginUnitStmt
import org.jetbrains.fortran.lang.psi.ext.endUnitStmt
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranProgramUnitNameMismatchInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Program unit name mismatch"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitProgramUnit(unit: FortranProgramUnit) {
                    val stmtName = unit.beginUnitStmt?.entityDecl?.name
                    val endStmtDataPath = unit.endUnitStmt?.dataPath

                    if (endStmtDataPath != null && !endStmtDataPath.referenceName.equals(stmtName, true)) {
                        registerProblemForReference(
                                holder,
                                endStmtDataPath.reference,
                                ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                                "Program unit name mismatch",
                                SubstituteTextFix(endStmtDataPath.smartPointer(), stmtName, "Fix unit name")
                        )
                    }
                }
            }
}
