package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.openapi.util.io.StreamUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.intellij.lang.annotations.Language

abstract class FortranInspectionsBaseTestCase(
        val inspection: LocalInspectionTool
) : LightPlatformCodeInsightFixtureTestCase() {
    protected fun checkByText(
            @Language("Fortran") text: String,
            checkWarn: Boolean = true, checkInfo: Boolean = false, checkWeakWarn: Boolean = false
    ) {
        myFixture.configureByText("program.f95", text)
        enableInspection()
        myFixture.checkHighlighting(checkWarn, checkInfo, checkWeakWarn)
    }

    protected fun checkFixByText(
            fixName: String,
            @Language("Fortran") before: String,
            @Language("Fortran") after: String,
            checkWarn: Boolean = true, checkInfo: Boolean = false, checkWeakWarn: Boolean = false
    ) {
        myFixture.configureByText("program.f95", before)
        enableInspection()
        myFixture.checkHighlighting(checkWarn, checkInfo, checkWeakWarn)
        applyQuickFix(fixName)
        myFixture.checkResult(after)
    }

    fun testInspectionHasDocumentation() {
        val description = "inspectionDescriptions/${inspection.javaClass.simpleName?.dropLast("Inspection".length)}.html"
        val text = getResourceAsString(description)
                ?: error("No inspection description for ${inspection.javaClass} ($description)")
        checkHtmlStyle(text)
    }

    protected fun enableInspection() =
            myFixture.enableInspections(inspection.javaClass)

    protected fun applyQuickFix(name: String) {
        val action = myFixture.findSingleIntention(name)
        myFixture.launchAction(action)
    }

    companion object {
        @JvmStatic
        fun checkHtmlStyle(html: String) {
            // http://stackoverflow.com/a/1732454
            val re = "<body>(.*)</body>".toRegex(RegexOption.DOT_MATCHES_ALL)
            val body = (re.find(html)?.let { it.groups[1]!!.value } ?: html).trim()
            check(body[0].isUpperCase()) {
                "Please start description with the capital latter"
            }

            check(body.last() == '.') {
               "Please end description with a period"
            }
        }

        @JvmStatic fun getResourceAsString(path: String): String? {
            val stream = FortranInspectionsBaseTestCase::class.java.classLoader.getResourceAsStream(path)
                    ?: return null

            return StreamUtil.readText(stream, Charsets.UTF_8)
        }
    }
}

