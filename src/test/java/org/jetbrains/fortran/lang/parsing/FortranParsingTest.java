package org.jetbrains.fortran.lang.parsing;

import org.jetbrains.fortran.test.FortranTestDataFixture;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("all")
public class FortranParsingTest extends FortranBaseParsingTestCase {
   public void testSimpleProgram() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("SimpleProgram.f95"));
   }
   public void testIfStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("IfStatements.f95"));
   }

    public void testdoConstract() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("doConstract.f95"));
    }

    public void testBlock() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Block.f95"));
    }

    public void testSelect() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Select.f95"));
    }

    public void testArraySpec() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ArraysSpec.f95"));
    }
    public void testPrintWrite() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("PrintWrite.f95"));
    }

    public void testExpressions() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Expressions.f95"));
    }

    public void testFileWork() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("FileWork.f95"));
    }

    public void testForall() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Forall.f95"));
    }

    public void testSync() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Sync.f95"));
    }

    public void testLiterals() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Literals.f95"));
    }

    public void testTypes() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Types.f95"));
    }

    public void testSpecificationStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("SpecificationStatements.f95"));
    }

    public void testExecutableStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ExecutableStatements.f95"));
    }

    public void testVariables() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Variables.f95"));
    }

    public void testProgramWithoutName() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ProgramWithoutName.f95"));
    }

    public void testLabels() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Labels.f95"));
    }

    public void testTopLevelConstructs() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("TopLevelConstructs.f95"));
    }

    public void testModule() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Module.f95"));
    }

    public void testFormat() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Format.f95"));
    }

    public void testOOP() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("OOP.f95"));
    }

    public void testInterface() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Interface.f95"));
    }

    public void testLabeledDo() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("LabeledDo.f95"));
    }

    public void testKeywords() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Keywords.f95"));
    }

    public void testFixedForm() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("FixedForm.f"));
    }
}