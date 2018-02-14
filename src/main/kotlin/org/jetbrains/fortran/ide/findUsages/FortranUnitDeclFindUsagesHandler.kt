package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.FortranUnitDecl
import org.jetbrains.fortran.lang.psi.impl.FortranUnitDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranUnitImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranUnitImplMixin
import org.jetbrains.fortran.lang.resolve.FortranUnitReferenceImpl
import java.util.*


class FortranUnitDeclFindUsagesHandler(
        element: FortranUnitDecl,
        factory: FortranFindUsagesHandlerFactory
) : FortranFindUsagesHandler<FortranUnitDecl>(element, factory) {
    override fun processElementUsages(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {
        return searchReferences(element, processor, options)
    }

    private fun searchReferences(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {
        val searcher = createSearcher(element, processor, options)
        if (!runReadAction { searcher.buildTaskList() })
            return false
        return searcher.executeTasks()
    }

    override fun findReferencesToHighlight(target: PsiElement, searchScope: SearchScope): Collection<PsiReference> {
        val results = Collections.synchronizedList(arrayListOf<PsiReference>())
        val options = findUsagesOptions.clone()
        options.searchScope = searchScope
        searchReferences(target, Processor { info ->
            val reference = info.reference
            if (reference != null) {
                results.add(reference)
            }
            true
        }, options)
        return results
    }

    private abstract class Searcher(val element: PsiElement, val processor: Processor<UsageInfo>, val options: FindUsagesOptions) {
        private val tasks = ArrayList<() -> Boolean>()

        protected fun addTask(task: () -> Boolean) {
            tasks.add(task)
        }

        fun executeTasks(): Boolean {
            return tasks.all { it() }
        }

        abstract fun buildTaskList(): Boolean
    }

    companion object {
        internal fun processUsage(processor: Processor<UsageInfo>, ref: PsiReference): Boolean =
                processor.processIfNotNull { if (ref.element.isValid) UsageInfo(ref) else null }


        private fun Processor<UsageInfo>.processIfNotNull(callback: () -> UsageInfo?): Boolean {
            val usageInfo = runReadAction(callback)
            return if (usageInfo != null) process(usageInfo) else true
        }
    }


    private fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Searcher {
        return object: Searcher(element, processor, options) {
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

    override fun calculateScope(element: PsiElement, searchScope: SearchScope) : SearchScope = searchScope
}