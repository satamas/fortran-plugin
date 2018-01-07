package org.jetbrains.fortran.ide.typing

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class FortranEnterHandlerTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath(): String = "src/test/resources/enterhandler/"

    fun testProgram() = doTest()

    private fun doTest() {
        myFixture.configureByFile(getTestName(false) + ".f95")
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(false) + "_after.f95", true)
    }
}