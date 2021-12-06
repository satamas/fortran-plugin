package org.jetbrains.fortran.ide.typing

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FortranEnterHandlerTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/resources/enterhandler/"

    fun testProgram() = doTest()
    fun testInsideProgramStmt() = doTest()
    fun testInsideProgramName() = doTest()
    fun testDo() = doTest()
    fun testEnum() = doTest()
    fun testLabelDo() = doTest()
    fun testNestedSubroutine() = doTest()

    private fun doTest() {
        myFixture.configureByFile(getTestName(false) + ".f95")
        myFixture.type('\n')
        myFixture.checkResultByFile(getTestName(false) + "_after.f95", true)
    }
}