package org.jetbrains.fortran.ide.findUsages

import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranLabelImplMixin
import org.jetbrains.fortran.lang.resolve.FortranLabelReferenceImpl


class FortranLabelDeclFindUsagesHandler (
        element: FortranLabelDecl,
        factory: FortranFindUsagesHandlerFactory
) : FortranLabelOrUnitDeclFindUsagesHandler<FortranLabelDecl>(element, factory) {
    override fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>): Searcher {
        return object: Searcher() {
            override fun buildTaskList(): Boolean {
                addTask {
                    runReadAction{ PsiTreeUtil.findChildrenOfType(
                            PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) , FortranLabelImpl::class.java)
                    }.filter { (element as FortranLabelDeclImpl).getLabelValue() == it.getLabelValue() }
                     .map{ FortranLabelReferenceImpl(it as FortranLabelImplMixin) }
                     .all { processUsage(processor, it) }
                }
                return true
            }
        }
    }
}