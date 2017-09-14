package org.jetbrains.fortran.ide.formatter

@SuppressWarnings("all")
class FortranFFormattingTest : FortranFixedFormFormattingTest() {
    @Throws(Exception::class)
    fun testHfunction() = doTest()
}