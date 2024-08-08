package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.*

class FortranStmtOrderInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Unallowed statement"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitBlock(block: FortranBlock) {
                    val blockOwner = block.parent ?: return

                    when (blockOwner) {
                        is FortranForallConstruct -> visitForallBlock(block)
                        is FortranWhereConstruct -> visitWhereBlock(block)
                        is FortranExecutableConstruct -> visitFortranExecutableConstructBlock(block)
                        is FortranProgramUnit -> {
                            if (blockOwner.parent is FortranInterfaceSpecification) {
                                visitProgramUnitInInterface(block)
                            } else if (blockOwner is FortranModule || blockOwner is FortranSubmodule
                                    || blockOwner is FortranBlockData) {
                                visitModuleLikeProgramUnit(block)
                            } else if (blockOwner is FortranMainProgram || blockOwner is FortranFunctionSubprogram
                                    || blockOwner is FortranSubroutineSubprogram || blockOwner is FortranSeparateModuleSubprogram) {
                                vistiExecutionProgramUnit(block)
                            }
                        }
                    }
                }

                private fun visitForallBlock(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranAssignmentStmt && stmt !is FortranPointerAssignmentStmt
                                && stmt !is FortranForallStmt && stmt !is FortranWhereStmt) {
                            registerProblem(holder, stmt, "This statement is not allowed in forall construct")
                        } else if ((stmt is FortranExecutableConstruct || stmt is FortranDeclarationConstruct)
                                && stmt !is FortranForallConstruct && stmt !is FortranWhereConstruct) {
                            registerProblem(holder, stmt, "This construct is not allowed in forall construct")
                        }
                    }
                }

                private fun visitWhereBlock(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranAssignmentStmt && stmt !is FortranWhereStmt) {
                            registerProblem(holder, stmt, "This statement is not allowed in where construct")
                        } else if ((stmt is FortranExecutableConstruct || stmt is FortranDeclarationConstruct)
                                && stmt !is FortranWhereConstruct) {
                            registerProblem(holder, stmt, "This construct is not allowed in where construct")
                        }
                    }
                }

                private fun visitFortranExecutableConstructBlock(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt && stmt !is FortranActionStmt && stmt !is FortranFormatStmt
                                && stmt !is FortranEntryStmt && stmt !is FortranDataStmt && stmt !is FortranIncludeStmt) {
                            registerProblem(holder, stmt, "Specification statement is not allowed here")
                        } else if (stmt is FortranDeclarationConstruct) {
                            registerProblem(holder, stmt, "Declaration construct is not allowed in executable construct")
                        }

                    }
                }

                private fun visitProgramUnitInInterface(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranStmt
                                && stmt !is FortranUseStmt
                                && stmt !is FortranImportStmt
                                && stmt !is FortranDeclarationStmt && stmt !is FortranIncludeStmt
                                && stmt !is FortranImplicitStmt
                                ) {
                            registerProblem(holder, stmt, "This statement is not allowed inside the interface")
                        } else if (stmt is FortranExecutableConstruct) {
                            registerProblem(holder, stmt, "Executable construct is not allowed in interface")
                        }

                    }
                }

                private fun visitModuleLikeProgramUnit(block: FortranBlock) {
                    for (stmt in block.children) {
                        if (stmt is FortranActionStmt) {
                            registerProblem(holder, stmt, "This statement is not allowed in this program unit")
                        } else if (stmt is FortranExecutableConstruct) {
                            registerProblem(holder, stmt, "Executable construct is not allowed in this program unit")
                        }

                    }
                }

                private fun vistiExecutionProgramUnit(block: FortranBlock) {
                    var level = BlockPart.USE_STATEMENTS
                    for (stmt in block.children) {
                        level = upgradeLevel(level, stmt)
                        checkStmtLevel(level, stmt)
                    }
                }

                private fun upgradeLevel(level: BlockPart, stmt: PsiElement): BlockPart {
                    if (stmt is FortranIncludeStmt
                            || (stmt !is FortranStmt && stmt !is FortranDeclarationConstruct && stmt !is FortranExecutableConstruct)) {
                        return level
                    }
                    if (stmt is FortranUseStmt) {
                        return level
                    }
                    if (stmt is FortranImportStmt) {
                        return maxOf(level, BlockPart.IMPORT_STATEMENTS)
                    }
                    if (stmt is FortranImplicitStmt || stmt is FortranParameterStmt
                            || stmt is FortranFormatStmt || stmt is FortranEntryStmt) {
                        return maxOf(level, BlockPart.IMPLICIT_STATEMENTS)
                    }
                    if (stmt is FortranDeclarationStmt || stmt is FortranDeclarationConstruct) {
                        return maxOf(level, BlockPart.DECLARATION_PART)
                    }
                    return BlockPart.EXECUTION_PART
                }

                private fun checkStmtLevel(level: BlockPart, stmt: PsiElement) {
                    if (stmt is FortranUseStmt && level > BlockPart.USE_STATEMENTS) {
                        registerProblem(holder, stmt, "Use statement is not allowed here")
                    } else if (stmt is FortranImportStmt && level > BlockPart.IMPORT_STATEMENTS) {
                        registerProblem(holder, stmt, "Import statement is not allowed here")
                    } else if (stmt is FortranImplicitStmt && level > BlockPart.IMPLICIT_STATEMENTS) {
                        registerProblem(holder, stmt, "Implicit statement must be in implicit part")
                    } else if (stmt is FortranDeclarationStmt && stmt !is FortranEntryStmt && stmt !is FortranFormatStmt
                            && level == BlockPart.EXECUTION_PART) {
                        registerProblem(holder, stmt, "Specification statement must be in specification part")
                    } else if (stmt is FortranDeclarationConstruct && level == BlockPart.EXECUTION_PART) {
                        registerProblem(holder, stmt, "Declaration construct must be in specification part")
                    }
                }
            }

    private enum class BlockPart {
        USE_STATEMENTS,
        IMPORT_STATEMENTS,
        IMPLICIT_STATEMENTS,
        DECLARATION_PART,
        EXECUTION_PART
    }
}