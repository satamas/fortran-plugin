package org.jetbrains.fortran.ide.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.fortran.lang.FortranTestUtils
import org.junit.Test

abstract class FortranInspectionsBaseTestCase(val inspection: LocalInspectionTool) : BasePlatformTestCase() {
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

    @Test
    fun testInspectionHasDocumentation() {
        val description = "inspectionDescriptions/${inspection.javaClass.simpleName?.dropLast("Inspection".length)}.html"
        val text = FortranTestUtils.getResourceAsString(description)
            ?: error("No inspection description for ${inspection.javaClass} ($description)")
        FortranTestUtils.checkHtmlStyle(text)
    }

    private fun enableInspection() =
            myFixture.enableInspections(inspection.javaClass)

    private fun applyQuickFix(name: String) {
        val action = myFixture.findSingleIntention(name)
        myFixture.launchAction(action)
    }
}

