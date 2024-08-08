package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranConstructNameImpl


class FortranUnusedConstructNameInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): FortranVisitor {
        return object : FortranVisitor() {

            override fun visitConstructNameDecl(name: FortranConstructNameDecl) {
                val unit = runReadAction{ PsiTreeUtil.getParentOfType(name, FortranProgramUnit::class.java)}
                val results = runReadAction{ PsiTreeUtil.findChildrenOfType(unit, FortranConstructNameImpl::class.java)}
                        .filter { (name as FortranConstructNameDeclImpl).getLabelValue().equals(it.getLabelValue(), true) }

                if (results.isEmpty())  {
                    val lastElement = if (name.nextSibling.node.elementType != TokenType.WHITE_SPACE) name else name.nextSibling
                    registerProblem(
                            holder, name,
                            "Unused construct name",
                            ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                            SubstituteTextFix(name.smartPointer(), lastElement.smartPointer(), "", "Delete construct name")
                    )
                }
            }
        }
    }
}