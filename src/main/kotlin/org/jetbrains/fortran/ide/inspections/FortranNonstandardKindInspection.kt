package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranConstant
import org.jetbrains.fortran.lang.psi.FortranIntrinsicTypeSpec
import org.jetbrains.fortran.lang.psi.FortranNonstandardKindSelector
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer

class FortranNonstandardKindInspection : LocalInspectionTool() {
    override fun getDisplayName() = "Nonstandard Kind Selector"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
            object : FortranVisitor() {
                override fun visitNonstandardKindSelector(kindSelector: FortranNonstandardKindSelector) {
                    val spec = kindSelector.parent as FortranIntrinsicTypeSpec

                    val kindSelectorText = kindSelector.expr?.text
                    val newKindSelector = if (spec.text.toLowerCase().contains("complex")) {
                        if ((kindSelector.expr as? FortranConstant)?.integerliteral != null) {
                            "(kind=${(kindSelectorText?.toInt()?.div(2).toString())})"
                        } else {
                            "(kind=($kindSelectorText)/2)"
                        }
                    } else {
                        "(kind=$kindSelectorText)"
                    }
                    holder.registerProblem(kindSelector,
                            "Obsolete operator",
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            SubstituteTextFix(kindSelector.smartPointer(), newKindSelector, "Nonstandard Kind Selector fix")
                    )
                }
            }
}