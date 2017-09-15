package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.*

class FortranProgramUnitNameMismatchInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Program unit name mismatch"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitProgramUnit(unit: FortranProgramUnit) {
                    val stmtName = when(unit) {
                        is FortranFunctionSubprogram -> unit.functionStmt.entityDecl?.name
                        is FortranSubroutineSubprogram -> unit.subroutineStmt.entityDecl?.name
                        is FortranModule -> unit.moduleStmt.entityDecl?.name
                        is FortranSubmodule -> unit.submoduleStmt.entityDecl?.name
                        is FortranBlockData -> unit.blockDataStmt.entityDecl?.name
                        is FortranMainProgram -> unit.programStmt?.entityDecl?.name
                        else -> null
                    }
                    val endStmtDataPath = when(unit) {
                        is FortranFunctionSubprogram -> unit.endFunctionStmt?.dataPath
                        is FortranSubroutineSubprogram -> unit.endSubroutineStmt?.dataPath
                        is FortranModule -> unit.endModuleStmt?.dataPath
                        is FortranSubmodule -> unit.endSubmoduleStmt?.dataPath
                        is FortranBlockData -> unit.endBlockDataStmt?.dataPath
                        is FortranMainProgram -> unit.endProgramStmt?.dataPath
                        else -> null
                    }

                    if (endStmtDataPath != null && !endStmtDataPath.referenceName.equals(stmtName, false)) {
                        holder.registerProblemForReference(endStmtDataPath.reference,
                                ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                                "Program unit name mismatch",
                                SubstituteTextFix(endStmtDataPath.textRange, stmtName, "Program unit name fix")
                        )
                    }
                }
            }
}
