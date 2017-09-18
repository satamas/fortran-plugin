package org.jetbrains.fortran.ide.annotator

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.intellij.lang.annotations.Language

class RsErrorAnnotatorTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath(): String = "src/test/annotator"

    private fun checkErrors(@Language("Fortran") text: String) {
        myFixture.configureByText("fortran.f95", text)
        myFixture.testHighlighting(false, false, false)
    }

    fun testDuplicateLabels() = checkErrors("""
        program testDuplicateLabels
            goto 1
            <error descr="Duplicated label `1` declaration">1</error> write(*,*) "A"
            <error descr="Duplicated label `1` declaration">1</error> write(*,*) "B"
        end
        """)

    fun testDuplicateConstructs() = checkErrors("""
        program testDuplicateConstructs
            <error descr="Duplicated construct name `myDo` declaration">myDo:</error> do i =1,3
            <error descr="Duplicated construct name `myDo` declaration">myDo:</error> do j =1,3
                write(*,*) i+j
            enddo
            enddo
        end
        """)
}