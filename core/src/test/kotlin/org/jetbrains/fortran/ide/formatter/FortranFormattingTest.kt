package org.jetbrains.fortran.ide.formatter

import org.junit.Test

@SuppressWarnings("all")
class FortranFormattingTest : FortranBaseFormattingTestCase() {
    @Throws(Exception::class)
    @Test
    fun testSimpleBlock() = doTest()

    @Test
    fun testLineContinue() = doTest()

    @Test
    fun testLines() = doTest()

    @Test
    fun testForall() = doTest()

    @Test
    fun testTypes() = doTest()

    @Test
    fun testUnnamedProgram() = doTest()

    @Test
    fun testOperator() = doTest()

    @Test
    fun testColons() = doTest()
}
