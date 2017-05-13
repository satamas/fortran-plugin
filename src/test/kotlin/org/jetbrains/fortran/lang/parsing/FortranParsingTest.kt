package org.jetbrains.fortran.lang.parsing

import org.jetbrains.fortran.test.FortranTestDataFixture
import org.junit.Test

@SuppressWarnings("all")
class FortranParsingTest : FortranBaseParsingTestCase() {
    @Test
    @Throws(Exception::class)
    fun testSimpleProgram() = doParsingTest(FortranTestDataFixture().navigationMetadata("SimpleProgram.f95"))

    @Throws(Exception::class)
    fun testIfStatements() = doParsingTest(FortranTestDataFixture().navigationMetadata("IfStatements.f95"))

    @Throws(Exception::class)
    fun testdoConstract() = doParsingTest(FortranTestDataFixture().navigationMetadata("doConstract.f95"))

    @Throws(Exception::class)
    fun testBlock() = doParsingTest(FortranTestDataFixture().navigationMetadata("Block.f95"))

    @Throws(Exception::class)
    fun testSelect() = doParsingTest(FortranTestDataFixture().navigationMetadata("Select.f95"))

    @Throws(Exception::class)
    fun testArraySpec() = doParsingTest(FortranTestDataFixture().navigationMetadata("ArraysSpec.f95"))

    @Throws(Exception::class)
    fun testPrintWrite() = doParsingTest(FortranTestDataFixture().navigationMetadata("PrintWrite.f95"))

    @Throws(Exception::class)
    fun testExpressions() = doParsingTest(FortranTestDataFixture().navigationMetadata("Expressions.f95"))

    @Throws(Exception::class)
    fun testFileWork() = doParsingTest(FortranTestDataFixture().navigationMetadata("FileWork.f95"))

    @Throws(Exception::class)
    fun testForall() = doParsingTest(FortranTestDataFixture().navigationMetadata("Forall.f95"))

    @Throws(Exception::class)
    fun testSync() = doParsingTest(FortranTestDataFixture().navigationMetadata("Sync.f95"))

    @Throws(Exception::class)
    fun testLiterals()= doParsingTest(FortranTestDataFixture().navigationMetadata("Literals.f95"))

    @Throws(Exception::class)
    fun testTypes() = doParsingTest(FortranTestDataFixture().navigationMetadata("Types.f95"))

    @Throws(Exception::class)
    fun testSpecificationStatements() = doParsingTest(FortranTestDataFixture().navigationMetadata("SpecificationStatements.f95"))

    @Throws(Exception::class)
    fun testExecutableStatements() = doParsingTest(FortranTestDataFixture().navigationMetadata("ExecutableStatements.f95"))

    @Throws(Exception::class)
    fun testVariables() = doParsingTest(FortranTestDataFixture().navigationMetadata("Variables.f95"))

    @Throws(Exception::class)
    fun testProgramWithoutName() = doParsingTest(FortranTestDataFixture().navigationMetadata("ProgramWithoutName.f95"))

    @Throws(Exception::class)
    fun testLabels() = doParsingTest(FortranTestDataFixture().navigationMetadata("Labels.f95"))

    @Throws(Exception::class)
    fun testTopLevelConstructs() = doParsingTest(FortranTestDataFixture().navigationMetadata("TopLevelConstructs.f95"))

    @Throws(Exception::class)
    fun testModule() = doParsingTest(FortranTestDataFixture().navigationMetadata("Module.f95"))

    @Throws(Exception::class)
    fun testFormat() = doParsingTest(FortranTestDataFixture().navigationMetadata("Format.f95"))

    @Throws(Exception::class)
    fun testOOP() = doParsingTest(FortranTestDataFixture().navigationMetadata("OOP.f95"))

    @Throws(Exception::class)
    fun testInterface() = doParsingTest(FortranTestDataFixture().navigationMetadata("Interface.f95"))

    @Throws(Exception::class)
    fun testLabeledDo() = doParsingTest(FortranTestDataFixture().navigationMetadata("LabeledDo.f95"))

    @Throws(Exception::class)
    fun testKeywords() = doParsingTest(FortranTestDataFixture().navigationMetadata("Keywords.f95"))

    @Throws(Exception::class)
    fun testFixedForm() = doParsingTest(FortranTestDataFixture().navigationMetadata("FixedForm.f"))

    @Throws(Exception::class)
    fun testSubroutine() = doParsingTest(FortranTestDataFixture().navigationMetadata("subroutine.f95"))

    @Throws(Exception::class)
    fun testIssue2() = doParsingTest(FortranTestDataFixture().navigationMetadata("Issue2.f95"))

}