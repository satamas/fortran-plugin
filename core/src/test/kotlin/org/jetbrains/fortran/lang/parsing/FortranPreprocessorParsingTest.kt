package org.jetbrains.fortran.lang.parsing

import org.jetbrains.fortran.test.FortranTestDataFixture
import org.junit.Test

class FortranPreprocessorParsingTest : FortranBaseParsingTestCase() {
    @Test
    fun testSimpleProgram() = doPreprocessorParsingTest(
        FortranTestDataFixture().navigationMetadata("DirectiveBasicSupport.f95")
    )
}