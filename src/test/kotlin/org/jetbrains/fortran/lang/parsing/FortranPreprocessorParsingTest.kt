package org.jetbrains.fortran.lang.parsing

import org.jetbrains.fortran.test.FortranTestDataFixture

class FortranPreprocessorParsingTest : FortranBaseParsingTestCase() {
    fun testSimpleProgram() = doPreprocessorParsingTest(
            FortranTestDataFixture().navigationMetadata("DirectiveBasicSupport.f95")
    )
}