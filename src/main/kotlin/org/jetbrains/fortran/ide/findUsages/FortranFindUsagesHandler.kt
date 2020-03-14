package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.ReadActionProcessor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor

abstract class FortranFindUsagesHandler<out T : PsiElement>(
        psiElement: T, val factory: FortranFindUsagesHandlerFactory
) : FindUsagesHandler(psiElement) {

    @Suppress("UNCHECKED_CAST")
    fun getElement(): T {
        return psiElement as T
    }

    override fun processElementUsages(
            element: PsiElement, processor: Processor<in UsageInfo>, options: FindUsagesOptions
    ): Boolean {
        val refProcessor = object : ReadActionProcessor<PsiReference>() {
            override fun processInReadAction(ref: PsiReference): Boolean {
                val rangeInElement = ref.rangeInElement
                return processor.process(UsageInfo(ref.element, rangeInElement.startOffset, rangeInElement.endOffset, false))
            }
        }

        val scope = calculateScope(element, options.searchScope)

        val searchText = options.isSearchForTextOccurrences && scope is GlobalSearchScope

        if (options.isUsages) {
            val success = ReferencesSearch.search(ReferencesSearch.SearchParameters(element, scope, false, options.fastTrack)).forEach(refProcessor)
            if (!success) return false
        }

        if (searchText) {
            if (options.fastTrack != null) {
                options.fastTrack.searchCustom { _ -> processUsagesInText(element, processor, scope as GlobalSearchScope) }
            } else {
                return processUsagesInText(element, processor, scope as GlobalSearchScope)
            }
        }
        return true
    }

    protected  abstract  fun calculateScope(element: PsiElement, searchScope: SearchScope) : SearchScope

}