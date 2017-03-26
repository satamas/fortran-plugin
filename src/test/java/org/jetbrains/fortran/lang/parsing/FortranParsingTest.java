package org.jetbrains.fortran.lang.parsing;

import org.jetbrains.fortran.test.FortranTestDataFixture;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("all")
public class FortranParsingTest extends FortranBaseParsingTestCase {
   public void testSimpleProgram() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("SimpleProgram.f"));
   }
   public void testIfStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("IfStatements.f"));
   }

    public void testdoConstract() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("doConstract.f"));
    }

    public void testBlock() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Block.f"));
    }

    public void testSelect() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Select.f"));
    }

    public void testArraySpec() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ArraysSpec.f"));
    }
    public void testPrintWrite() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("PrintWrite.f"));
    }

    public void testExpressions() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Expressions.f"));
    }

    public void testFileWork() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("FileWork.f"));
    }
 /*   public void testSpecificationStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("SpecificationStatements.f"));
    }

    public void testExecutableStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ExecutableStatements.f"));
    }

    public void testVariables() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Variables.f"));
    }
*/
    public void testProgramWithoutName() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ProgramWithoutName.f"));
    }

  /*  public void testLabels() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Labels.f"));
    }

    public void testFunction() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("TopLevelConstructs.f"));
    }*/

}