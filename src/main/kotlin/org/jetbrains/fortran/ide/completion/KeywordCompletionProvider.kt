package org.jetbrains.fortran.ide.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class KeywordCompletionProvider(val keywords: Collection<String>) : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
        result.addAllElements(keywords.map { getLookupElement(it) })
    }

    private fun getLookupElement(keyword: String): LookupElement {
        val builder = LookupElementBuilder.create(keyword)
        builder.withPriority(KEYWORD_PRIORITY)
        return builder
    }
}