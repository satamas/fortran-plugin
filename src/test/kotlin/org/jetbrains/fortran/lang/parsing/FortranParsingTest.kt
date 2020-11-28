package org.jetbrains.fortran.lang.parsing

import org.jetbrains.fortran.test.FortranTestDataFixture
import org.junit.Test

@SuppressWarnings("all")
class FortranParsingTest : FortranBaseParsingTestCase() {
    @Throws(Exception::class)
    @Test
    fun testSimpleProgram() = doParsingTest(FortranTestDataFixture().navigationMetadata("SimpleProgram.f95"))

    @Throws(Exception::class)
    @Test
    fun testIfStatements() = doParsingTest(FortranTestDataFixture().navigationMetadata("IfStatements.f95"))

    @Throws(Exception::class)
    @Test
    fun testdoConstract() = doParsingTest(FortranTestDataFixture().navigationMetadata("doConstract.f95"))

    @Throws(Exception::class)
    @Test
    fun testBlock() = doParsingTest(FortranTestDataFixture().navigationMetadata("Block.f95"))

    @Throws(Exception::class)
    @Test
    fun testSelect() = doParsingTest(FortranTestDataFixture().navigationMetadata("Select.f95"))

    @Throws(Exception::class)
    @Test
    fun testArraySpec() = doParsingTest(FortranTestDataFixture().navigationMetadata("ArraysSpec.f95"))

    @Throws(Exception::class)
    @Test
    fun testPrintWrite() = doParsingTest(FortranTestDataFixture().navigationMetadata("PrintWrite.f95"))

    @Throws(Exception::class)
    @Test
    fun testExpressions() = doParsingTest(FortranTestDataFixture().navigationMetadata("Expressions.f95"))

    @Throws(Exception::class)
    @Test
    fun testFileWork() = doParsingTest(FortranTestDataFixture().navigationMetadata("FileWork.f95"))

    @Throws(Exception::class)
    @Test
    fun testForall() = doParsingTest(FortranTestDataFixture().navigationMetadata("Forall.f95"))

    @Throws(Exception::class)
    @Test
    fun testSync() = doParsingTest(FortranTestDataFixture().navigationMetadata("Sync.f95"))

    @Throws(Exception::class)
    @Test
    fun testLiterals()= doParsingTest(FortranTestDataFixture().navigationMetadata("Literals.f95"))

    @Throws(Exception::class)
    @Test
    fun testTypes() = doParsingTest(FortranTestDataFixture().navigationMetadata("Types.f95"))

    @Throws(Exception::class)
    @Test
    fun testSpecificationStatements() = doParsingTest(FortranTestDataFixture().navigationMetadata("SpecificationStatements.f95"))

    @Throws(Exception::class)
    @Test
    fun testExecutableStatements() = doParsingTest(FortranTestDataFixture().navigationMetadata("ExecutableStatements.f95"))

    @Throws(Exception::class)
    @Test
    fun testVariables() = doParsingTest(FortranTestDataFixture().navigationMetadata("Variables.f95"))

    @Throws(Exception::class)
    @Test
    fun testProgramWithoutName() = doParsingTest(FortranTestDataFixture().navigationMetadata("ProgramWithoutName.f95"))

    @Throws(Exception::class)
    @Test
    fun testLabels() = doParsingTest(FortranTestDataFixture().navigationMetadata("Labels.f95"))

    @Throws(Exception::class)
    @Test
    fun testTopLevelConstructs() = doParsingTest(FortranTestDataFixture().navigationMetadata("TopLevelConstructs.f95"))

    @Throws(Exception::class)
    @Test
    fun testModule() = doParsingTest(FortranTestDataFixture().navigationMetadata("Module.f95"))

    @Throws(Exception::class)
    @Test
    fun testFormat() = doParsingTest(FortranTestDataFixture().navigationMetadata("Format.f95"))

    @Throws(Exception::class)
    @Test
    fun testOOP() = doParsingTest(FortranTestDataFixture().navigationMetadata("OOP.f95"))

    @Throws(Exception::class)
    @Test
    fun testIntent() = doParsingTest(FortranTestDataFixture().navigationMetadata("Intent.f95"))

    @Throws(Exception::class)
    @Test
    fun testInterface() = doParsingTest(FortranTestDataFixture().navigationMetadata("Interface.f95"))

    @Throws(Exception::class)
    @Test
    fun testLabeledDo() = doParsingTest(FortranTestDataFixture().navigationMetadata("LabeledDo.f95"))

    @Throws(Exception::class)
    @Test
    fun testKeywords() = doParsingTest(FortranTestDataFixture().navigationMetadata("Keywords.f95"))

    @Throws(Exception::class)
    @Test
    fun testFixedForm() = doParsingTest(FortranTestDataFixture().navigationMetadata("FixedForm.f"))

    @Throws(Exception::class)
    @Test
    fun testSubroutine() = doParsingTest(FortranTestDataFixture().navigationMetadata("subroutine.f95"))

    @Throws(Exception::class)
    @Test
    fun testUse() = doParsingTest(FortranTestDataFixture().navigationMetadata("Use.f95"))

    @Throws(Exception::class)
    @Test
    fun testOperator() = doParsingTest(FortranTestDataFixture().navigationMetadata("Operator.f95"))

    @Throws(Exception::class)
    @Test
    fun testSelectType() = doParsingTest(FortranTestDataFixture().navigationMetadata("SelectType.f95"))

    @Throws(Exception::class)
    @Test
    fun testArraySlices() = doParsingTest(FortranTestDataFixture().navigationMetadata("ArraySlices.f95"))

    fun testIncompleteFunction() = doParsingTest(FortranTestDataFixture().navigationMetadata("FunctionIncomplete.f95"))
}