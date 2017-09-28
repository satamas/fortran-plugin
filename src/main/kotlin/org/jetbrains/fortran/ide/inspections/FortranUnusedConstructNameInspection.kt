package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.TokenType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.ext.smartPointer



class FortranUnusedConstructNameInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): FortranVisitor {
        return object : FortranVisitor() {

            override fun visitConstructNameDecl(name: FortranConstructNameDecl) {
                if (ReferencesSearch.search(name, GlobalSearchScope.fileScope(name.containingFile)).findFirst() == null) {
                    val lastElement = if (name.nextSibling.node.elementType != TokenType.WHITE_SPACE) name else name.nextSibling
                    holder.registerProblem(name,
                            "Unused construct name",
                            ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                            SubstituteTextFix(name.smartPointer(), lastElement.smartPointer(), "", "Delete construct name")
                    )
                }
            }
        }
    }
}