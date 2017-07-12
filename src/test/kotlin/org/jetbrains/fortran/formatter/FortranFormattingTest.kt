package org.jetbrains.fortran.formatter

@SuppressWarnings("all")
class FortranFormattingTest : FortranBaseFormattingTestCase() {
    @Throws(Exception::class)
    fun testSimpleBlock() = doTest()
    fun testLineContinue() = doTest()
    fun testLines() = doTest()
    fun testForall() = doTest()
}
