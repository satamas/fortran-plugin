package org.jetbrains.fortran.ide.folding

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import java.io.File

class FortranFoldingBuilderTest : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources"

    override fun getBasePath() = "folding/"

    @Test
    fun testProgramUnits() = doTest()

    @Test
    fun testExecutableConstructs() = doTest()

    @Test
    fun testDeclarationConstructs() = doTest()

    @Test
    fun testLabeledDoConstruct() = doTest()

    @Test
    fun testEmptySubprogram() = doTest()

    private fun doTest() {
        myFixture.testFolding(testDataPath + File.separator + basePath + File.separator + getTestName(false) + ".f95")
    }
}
