package org.jetbrains.fortran.lang.parsing;

import org.jetbrains.fortran.test.FortranTestDataFixture;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("all")
public class FortranParsingTest extends FortranBaseParsingTestCase {

    public void testPrintWrite() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("PrintWrite.f"));
    }

    public void testExpressions() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Expressions.f"));
    }

    public void testSpecificationStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("SpecificationStatements.f"));
    }

    public void testExecutableStatements() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ExecutableStatements.f"));
    }

    public void testVariables() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Variables.f"));
    }

    public void testProgramWithoutName() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("ProgramWithoutName.f"));
    }

    public void testLabels() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Labels.f"));
    }

    public void testFunction() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("TopLevelConstructs.f"));
    }

}