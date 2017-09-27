package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.SmartPointerManager
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.ide.inspections.fixes.SubstituteTextFix
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranVisitor
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranLabelImplMixin
import org.jetbrains.fortran.lang.resolve.FortranLabelReferenceImpl

class FortranUnusedLabelInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): FortranVisitor {
        return object : FortranVisitor() {

            override fun visitLabelDecl(label: FortranLabelDecl) {
                // custom searcher for leading zeros
                val unit = PsiTreeUtil.getParentOfType(label, FortranProgramUnit::class.java)
                val results = runReadAction{ PsiTreeUtil.findChildrenOfType(unit, FortranLabelImpl::class.java)
                }.filter { (label as FortranLabelDeclImpl).getLabelValue() == it.getLabelValue() }
                        .map{ FortranLabelReferenceImpl(it as FortranLabelImplMixin) }

                if (results.isEmpty()) {
                    val whiteSpaceSize = if (label.nextSibling.node.elementType != TokenType.WHITE_SPACE) 0 else label.nextSibling.textLength
                    holder.registerProblem(label, "Unused label declaration", ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            SubstituteTextFix(SmartPointerManager.getInstance(label.project).createSmartPsiElementPointer(label),
                                    SmartPointerManager.getInstance(label.project).createSmartPsiElementPointer(label), "", "Delete label declaration"))
                }
            }
        }
    }
}