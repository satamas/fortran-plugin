package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.lang.psi.*

class FortranUnallowedConstructInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Unallowed construct"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitExecutableConstruct(construct: FortranExecutableConstruct) {
                    val block = construct.parent as? FortranBlock ?: return
                    val blockOwner = block.parent ?: return

                    when (blockOwner) {
                        is FortranForallConstruct -> {
                            if (construct !is FortranForallConstruct && construct !is FortranWhereConstruct) {
                                holder.registerProblem(construct, "This construct is not allowed in forall construct")
                            }
                        }
                        is FortranWhereConstruct -> {
                            if (construct !is FortranWhereConstruct) {
                                holder.registerProblem(construct, "This construct is not allowed in where construct")
                            }
                        }

                        is FortranModule -> holder.registerProblem(construct, "Executable construct is not allowed in module")
                        is FortranSubmodule -> holder.registerProblem(construct, "Executable construct is not allowed in submodule")
                        is FortranBlockData -> holder.registerProblem(construct, "Executable construct is not allowed in blockdata")

                        is FortranProgramUnit -> {
                            val programUnitOwner = blockOwner.parent

                            if (programUnitOwner is FortranInterfaceSpecification) {
                                holder.registerProblem(construct, "Executable construct is not allowed in interface")
                            }
                        }
                    }
                }

                override fun visitDeclarationConstruct(construct: FortranDeclarationConstruct) {
                    val block = construct.parent as? FortranBlock ?: return
                    val blockOwner = block.parent ?: return

                    if (blockOwner is FortranExecutableConstruct) {
                        holder.registerProblem(construct, "Declaration construct is not allowed in executable construct")
                    }
                }

            }
}