package org.jetbrains.fortran.ide.formatter

@SuppressWarnings("all")
class FortranFormattingTest : FortranBaseFormattingTestCase() {
    @Throws(Exception::class)
    fun testSimpleBlock() = doTest()
    fun testLineContinue() = doTest()
    fun testLines() = doTest()
    fun testForall() = doTest()
    fun testTypes() = doTest()
    fun testUnnamedProgram() = doTest()
    fun testOperator() = doTest()
    fun testColons() = doTest()
}
