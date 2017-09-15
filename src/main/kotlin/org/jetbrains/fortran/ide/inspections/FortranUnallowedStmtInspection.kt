package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.lang.psi.*

class FortranUnallowedStmtInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Unallowed statement"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitStmt(stmt: FortranStmt) {
                    if (stmt is FortranOnlyStmt || stmt is FortranRenameStmt) return
                    val block = stmt.parent as? FortranBlock ?: return
                    val blockOwner = block.parent ?: return

                    when (blockOwner) {
                        is FortranForallConstruct -> {
                            if (stmt !is FortranAssignStmt && stmt !is FortranPointerAssignmentStmt
                                    && stmt !is FortranForallStmt && stmt !is FortranWhereStmt) {
                                holder.registerProblem(stmt, "This statement is not allowed in forall construct")
                            }
                        }
                        is FortranWhereConstruct -> {
                            if (stmt !is FortranAssignStmt && stmt !is FortranWhereStmt) {
                                holder.registerProblem(stmt, "This statement is not allowed in where construct")
                            }
                        }
                    }

                    if ( stmt !is FortranImportStmt
                         && stmt !is FortranUseStmt && stmt !is FortranSpecificationStmt
                         && stmt !is FortranImplicitStmt && stmt !is FortranParameterStmt
                         && stmt !is FortranFormatStmt && stmt !is FortranEntryStmt) {
                        if (blockOwner is FortranModule || blockOwner is FortranSubmodule || blockOwner is FortranBlockData) {
                            holder.registerProblem(stmt, "This statement is not allowed in this program unit")
                        } else if (blockOwner.parent is FortranInterfaceSpecification){
                            holder.registerProblem(stmt, "This statement is not allowed inside the interface")
                        }
                    }
                }

                override fun visitSpecificationStmt(stmt: FortranSpecificationStmt) {
                    val block = stmt.parent as? FortranBlock ?: return
                    val blockOwner = block.parent ?: return

                    when (blockOwner) {
                        is FortranExecutableConstruct -> {
                            holder.registerProblem(stmt, "Specification statement is not allowed here")
                        }
                    }

                }
            }

}