package org.jetbrains.fortran.ide.folding

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import java.io.File

class FortranFoldingBuilderTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath() = "src/test/resources"

    override fun getBasePath() = "folding/"

    fun testProgramUnits() {
        doTest()
    }

    private fun doTest() {
        myFixture.testFolding(testDataPath + File.separator + basePath + File.separator + getTestName(false) + ".f95")
    }
}
