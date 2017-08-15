package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranLabelImplMixin
import org.jetbrains.fortran.lang.resolve.ref.FortranLabelReferenceImpl


class FortranLabelDeclFindUsagesHandler (
        element: FortranLabelDecl,
        factory: FortranFindUsagesHandlerFactory
) : FortranFindUsagesHandler<FortranLabelDecl>(element, factory) {

    override fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Searcher {
        val scope = runReadAction { GlobalSearchScope.fileScope(element.containingFile) }
        options.searchScope = scope
        return object: Searcher(element, processor, options) {
            override fun buildTaskList(): Boolean {
                addTask {
                    runReadAction{ PsiTreeUtil.findChildrenOfType( PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java) , FortranLabelImpl::class.java)}
                            .filter { (element as FortranLabelDeclImpl).gelLabelValue() == it.gelLabelValue() }
                            .map{ FortranLabelReferenceImpl(it as FortranLabelImplMixin) }
                            .all { processUsage(processor, it) }
                }
                return true
            }
        }
    }

}