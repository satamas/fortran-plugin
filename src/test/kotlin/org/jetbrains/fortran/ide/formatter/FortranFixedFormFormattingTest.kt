package org.jetbrains.fortran.ide.formatter

@SuppressWarnings("all")
class FortranFixedFormFormattingTest : FortranBaseFixedFormFormattingTestCase() {
    @Throws(Exception::class)
    fun testHfunction() = doTest()
}