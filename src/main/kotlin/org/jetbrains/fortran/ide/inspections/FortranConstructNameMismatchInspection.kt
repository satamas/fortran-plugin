package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.*

class FortranConstructNameMismatchInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Construct name mismatch"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitConstructName(name: FortranConstructName) {
                    if (name.parent !is FortranExitStmt) {
                        val construct = name.parent.parent
                        val realName = when(construct) {
                            is FortranAssociateConstruct -> construct.associateStmt.constructNameDecl?.name
                            is FortranBlockConstruct -> construct.blockStmt.constructNameDecl?.name
                            is FortranCaseConstruct -> construct.selectCaseStmt.constructNameDecl?.name
                            is FortranCriticalConstruct -> construct.criticalStmt.constructNameDecl?.name
                            is FortranNonlabelDoConstruct -> construct.nonlabelDoStmt.constructNameDecl?.name
                            is FortranLabeledDoConstruct -> construct.labelDoStmt.constructNameDecl?.name
                            is FortranForallConstruct -> construct.forallConstructStmt.constructNameDecl?.name
                            is FortranIfConstruct -> construct.ifThenStmt.constructNameDecl?.name
                            is FortranSelectTypeConstruct -> construct.selectTypeStmt.constructNameDecl?.name
                            is FortranWhereConstruct -> construct.whereConstructStmt.constructNameDecl?.name
                            else -> null
                        }

                        if (!name.referenceName.equals(realName, true)) {
                            holder.registerProblemForReference(name.reference,
                                    ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                                    "Construct name mismatch",
                                    SubstituteTextFix(name.textRange, realName, "Construct name fix")
                            )
                        }
                    }
                }
            }
}
