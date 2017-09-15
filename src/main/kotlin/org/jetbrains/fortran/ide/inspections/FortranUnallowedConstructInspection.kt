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

                        is FortranProgramUnit -> {
                            val programUnitOwner = blockOwner.parent

                            if (programUnitOwner is FortranInterfaceSpecification) {
                                holder.registerProblem(construct, "Executable construct is not allowed in interface")
                            }
                        }
                    }
                }

                override fun visitEnumDef(o: FortranEnumDef) {
                    super.visitEnumDef(o)
                }

                override fun visitTypeDecl(o: FortranTypeDecl) {
                    super.visitTypeDecl(o)
                }

                override fun visitInterfaceBlock(o: FortranInterfaceBlock) {
                    super.visitInterfaceBlock(o)
                }
            }

}