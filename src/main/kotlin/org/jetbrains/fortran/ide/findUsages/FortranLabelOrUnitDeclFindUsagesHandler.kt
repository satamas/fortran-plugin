package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchScope
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import java.util.*


abstract class FortranLabelOrUnitDeclFindUsagesHandler <out T : PsiElement>(
        element: T,
        factory: FortranFindUsagesHandlerFactory
) : FortranFindUsagesHandler<T>(element, factory) {
    override fun processElementUsages(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {
        return searchReferences(element, processor)
    }

    private fun searchReferences(element: PsiElement, processor: Processor<UsageInfo>): Boolean {
        val searcher = createSearcher(element, processor)
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
        })
        return results
    }

    protected abstract class Searcher {
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


    protected abstract fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>): Searcher

    override fun calculateScope(element: PsiElement, searchScope: SearchScope) : SearchScope = searchScope
}