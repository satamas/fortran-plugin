package org.jetbrains.fortran.lang.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.fortran.FortranFileType

abstract class FortranCompletionTestBase : LightPlatformCodeInsightFixtureTestCase() {
    protected fun doSingleCompletion(@Language("Fortran") before: String, @Language("Fortran") after: String) {
        myFixture.configureByText(FortranFileType, before)
        executeSoloCompletion()
        myFixture.checkResult(after)
    }

    protected fun checkNoCompletion(@Language("Fortran") code: String) {
        myFixture.configureByText(FortranFileType, code)
        noCompletionCheck()
    }

    private fun noCompletionCheck() {
        val variants = myFixture.completeBasic()
        checkNotNull(variants) {
            val element = myFixture.file.findElementAt(myFixture.caretOffset - 1)
            "Expected zero completions, but one completion was auto inserted: `${element?.text}`."
        }
        check(variants.isEmpty()) {
            "Expected zero completions, got ${variants.size}."
        }
    }

    protected fun executeSoloCompletion() {
        val variants = myFixture.completeBasic()

        if (variants != null) {
            if (variants.size == 1) {
                return
            }
            fun LookupElement.debug(): String = "$lookupString ($psiElement)"
            error("Expected a single completion, but got ${variants.size}\n"
                    + variants.joinToString("\n") { it.debug() })
        }
    }
}