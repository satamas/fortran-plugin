package org.jetbrains.fortran.ide.intentions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.fortran.lang.FortranTestUtils


abstract class FortranIntentionsBaseTestCase(val intention: IntentionAction) : BasePlatformTestCase() {
    fun testIntentionHasDocumentation() {
        val directory = "intentionDescriptions/${intention.javaClass.simpleName}"
        val files = listOf("before.f90.template", "after.f90.template", "description.html")
        for (file in files) {
            val text = FortranTestUtils.getResourceAsString("$directory/$file")
                    ?: error("No intention description for ${intention.javaClass} ($directory/$file)")

            if (file.endsWith(".html")) {
                FortranTestUtils.checkHtmlStyle(text)
            }
        }
    }

    protected fun testIntention(@Language("Fortran") before: String, @Language("Fortran") after: String) {
        myFixture.configureByText("program.f95", before)
        myFixture.launchAction(intention)
        myFixture.checkResult(after)
    }
}
