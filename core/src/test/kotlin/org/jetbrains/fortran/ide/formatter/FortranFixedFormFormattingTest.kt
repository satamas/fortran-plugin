package org.jetbrains.fortran.ide.formatter

import org.junit.Test

@SuppressWarnings("all")
class FortranFixedFormFormattingTest : FortranBaseFixedFormFormattingTestCase() {
    @Throws(Exception::class)
    @Test
    fun testHfunction() = doTest()
}