package org.jetbrains.fortran.ide.actions

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.jetbrains.fortran.ide.util.executeWriteCommand

class FortranFixedFormConverterActionTest : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/converter/"

    private fun doTest(fileName: String, fileAfterName: String) {
        val fixedFormFile = myFixture.configureByFile(fileName)
        myFixture.project.executeWriteCommand("Test convert file") {
            FortranFileConverter.convertFile(myFixture.project, fixedFormFile)
        }
        myFixture.checkResultByFile("Before.f90", fileAfterName, false)
    }

    fun testFreeFormComment() = doTest("Before.f", "After.f90")
}