package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.lang.psi.*

class FortranStmtOrderInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Unallowed statement"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitBlock(block: FortranBlock) {
                    val blockOwner = block.parent ?: return

                    when (blockOwner) {
                        is FortranForallConstruct -> visitForallBlock(block)
                        is FortranWhereConstruct  -> visitWhereBlock(block)
                        is FortranExecutableConstruct -> visitFortranExecutableConstructBlock(block)
                        is FortranProgramUnit -> {
                            if (blockOwner.parent is FortranInterfaceSpecification) {
                                visitProgramUnitInInterface(block)
                            } else if (blockOwner is FortranModule || blockOwner is FortranSubmodule
                                    || blockOwner is FortranBlockData) {
                                visitModuleLikeProgramUnit(block)
                            }
                        }
                    }

                   /* if ( stmt !is FortranImportStmt
                         && stmt !is FortranUseStmt && stmt !is FortranSpecificationStmt
                         && stmt !is FortranImplicitStmt && stmt !is FortranParameterStmt
                         && stmt !is FortranFormatStmt && stmt !is FortranEntryStmt) {
                        if (blockOwner is FortranModule || blockOwner is FortranSubmodule || blockOwner is FortranBlockData) {
                            } else if (blockOwner.parent is FortranInterfaceSpecification){
                            holder.registerProblem(stmt, "This statement is not allowed inside the interface")
                        }
                    }*/
                }

                private fun visitForallBlock(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranAssignStmt && stmt !is FortranPointerAssignmentStmt
                               && stmt !is FortranForallStmt && stmt !is FortranWhereStmt) {
                            holder.registerProblem(stmt, "This statement is not allowed in forall construct")
                        } else if ((stmt is FortranExecutableConstruct || stmt is FortranDeclarationConstruct)
                                && stmt !is FortranForallConstruct && stmt !is FortranWhereConstruct) {
                            holder.registerProblem(stmt, "This construct is not allowed in forall construct")
                        }
                    }
                }

                private fun visitWhereBlock(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranAssignStmt && stmt !is FortranWhereStmt) {
                            holder.registerProblem(stmt, "This statement is not allowed in where construct")
                        } else if ((stmt is FortranExecutableConstruct || stmt is FortranDeclarationConstruct)
                                && stmt !is FortranWhereConstruct) {
                            holder.registerProblem(stmt, "This construct is not allowed in where construct")
                        }
                    }
                }

                private fun visitFortranExecutableConstructBlock(block : FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranActionStmt) {
                            holder.registerProblem(stmt, "Specification statement is not allowed here")
                        } else if (stmt is FortranDeclarationConstruct) {
                            holder.registerProblem(stmt, "Declaration construct is not allowed in executable construct")
                        }

                    }
                }

                private fun visitProgramUnitInInterface(block : FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranUseStmt && stmt !is FortranImportStmt
                                && stmt !is FortranDeclarationStmt) {
                            holder.registerProblem(stmt, "This statement is not allowed inside the interface")
                        } else if (stmt is FortranExecutableConstruct) {
                            holder.registerProblem(stmt, "Executable construct is not allowed in interface")
                        }

                    }
                }

                private fun visitModuleLikeProgramUnit(block : FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranActionStmt) {
                            holder.registerProblem(stmt, "This statement is not allowed in this program unit")
                        } else if (stmt is FortranExecutableConstruct) {
                            holder.registerProblem(stmt, "Executable construct is not allowed in this program unit")
                        }

                    }
                }
            }

}