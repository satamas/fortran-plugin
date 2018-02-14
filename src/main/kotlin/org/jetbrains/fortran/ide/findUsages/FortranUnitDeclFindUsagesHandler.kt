package org.jetbrains.fortran.ide.findUsages

import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranUnitDecl
import org.jetbrains.fortran.lang.psi.impl.FortranUnitDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranUnitImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranUnitImplMixin
import org.jetbrains.fortran.lang.resolve.FortranUnitReferenceImpl


class FortranUnitDeclFindUsagesHandler(
        element: FortranUnitDecl,
        factory: FortranFindUsagesHandlerFactory
) : FortranLabelOrUnitDeclFindUsagesHandler<FortranUnitDecl>(element, factory) {
    override fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>): Searcher {
        return object: Searcher() {
            override fun buildTaskList(): Boolean {
                addTask {
                    runReadAction{ PsiTreeUtil.findChildrenOfType(
                            PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) , FortranUnitImpl::class.java)
                    }.filter { (element as FortranUnitDeclImpl).getUnitValue() == it.getUnitValue() }
                     .map{ FortranUnitReferenceImpl(it as FortranUnitImplMixin) }
                     .all { processUsage(processor, it) }
                }
                return true
            }
        }
    }
}