package org.jetbrains.fortran.ide.formatter

import org.intellij.lang.annotations.Language

abstract class FortranFixedFormFormattingTest : FortranBaseFormattingTestCase() {
    override fun getFileExtension() = "for"

    override fun doTextTest(@Language("Fortran fixed form") text: String, @Language("Fortran fixed form") textAfter: String) {
        super.doTextTest(text.trimIndent(), textAfter.trimIndent())
    }

}
