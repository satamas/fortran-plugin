package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranLabelImplMixin
import org.jetbrains.fortran.lang.resolve.FortranLabelReferenceImpl

class FortranUnusedConstructNameInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): FortranVisitor {
        return object : FortranVisitor() {

            override fun visitConstructNameDecl(name: FortranConstructNameDecl) {
                if (ReferencesSearch.search(name, GlobalSearchScope.fileScope(name.containingFile)).findFirst() == null) {
                    holder.registerProblem(name, "Unused construct name", ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            SubstituteTextFix(name.textRange, "", "Delete construct name"))
                }
            }
        }
    }
}