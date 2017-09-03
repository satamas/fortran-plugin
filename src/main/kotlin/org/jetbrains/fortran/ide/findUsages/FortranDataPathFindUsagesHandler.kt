package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.fortran.lang.psi.FortranDataPath

class FortranDataPathFindUsagesHandler (
        element: FortranDataPath,
        factory: FortranFindUsagesHandlerFactory
) : FortranFindUsagesHandler<FortranDataPath>(element, factory) {

    override fun createSearcher(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Searcher {
        val scope = runReadAction { GlobalSearchScope.fileScope(element.containingFile) }
        options.searchScope = scope
        return object: Searcher(element, processor, options) {
            override fun buildTaskList(): Boolean {
                addTask {
                    ReferencesSearch.search(element, options.searchScope).all { processUsage(processor, it) }
                }

                return true
            }
        }
    }

}