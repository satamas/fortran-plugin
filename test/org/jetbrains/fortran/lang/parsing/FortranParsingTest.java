package org.jetbrains.fortran.lang.parsing;

import org.jetbrains.fortran.test.FortranTestDataFixture;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@SuppressWarnings("all")
public class FortranParsingTest extends FortranBaseParsingTestCase {

    public void testPrintWrite() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("PrintWrite.f"));
    }

    public void testImplicit() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Implicit.f"));
    }

    public void testVariables() throws Exception {
        doParsingTest(FortranTestDataFixture.navigationMetadata("Variables.f"));
    }
}