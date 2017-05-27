package org.jetbrains.fortran.formatter

import com.intellij.psi.formatter.FormatterTestCase
import org.intellij.lang.annotations.Language

abstract class FortranBaseFormattingTestCase : FormatterTestCase() {
    override fun getTestDataPath() = "src/test/resources"

    override fun getBasePath() = "formatter/"

    override fun getFileExtension() = "f95"

    override fun getTestName(lowercaseFirstLetter: Boolean): String {
        return super.getTestName(false)
    }

    override fun doTextTest(@Language("Fortran") text: String, @Language("Fortran") textAfter: String) {
        super.doTextTest(text.trimIndent(), textAfter.trimIndent())
    }
}