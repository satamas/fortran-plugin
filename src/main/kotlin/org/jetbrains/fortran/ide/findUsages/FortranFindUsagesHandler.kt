package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchScope
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import java.util.*


abstract class FortranFindUsagesHandler <T : PsiElement>(psiElement: T,
       val factory: FortranFindUsagesHandlerFactory)
    : FindUsagesHandler(psiElement) {

    @Suppress("UNCHECKED_CAST") fun getElement(): T {
        return psiElement as T
    }

    override fun processElementUsages(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {
        return searchReferences(element, processor, options)
    }

    protected open fun searchReferences(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {
        val searcher = createSearcher(element, processor, options)
        if (!runReadAction { searcher.buildTaskList() })
                 return false
        return searcher.executeTasks()
    }

    protected abstract fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Searcher

    override fun findReferencesToHighlight(target: PsiElement, searchScope: SearchScope): Collection<PsiReference> {
        val results = Collections.synchronizedList(arrayListOf<PsiReference>())
        val options = findUsagesOptions.clone()
        options.searchScope = searchScope
        searchReferences(target, object : Processor<UsageInfo> {
            override fun process(info: UsageInfo): Boolean {
                val reference = info.reference
                if (reference != null) {
                    results.add(reference)
                }
                return true
            }
        }, options)
        return results
    }

    protected abstract class Searcher(val element: PsiElement, val processor: Processor<UsageInfo>, val options: FindUsagesOptions) {
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
}