package org.jetbrains.fortran.ide.typing

import com.intellij.codeInsight.highlighting.BraceMatchingUtil.getMatchedBraceOffset
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.intellij.lang.annotations.Language
import org.jetbrains.fortran.FortranFileType

class FortranBraceMatcherTest : BasePlatformTestCase() {
    fun testPar() = doMatch("""
             program a
             integer :: i
             do i=1,4
                write<caret>(*,*) i
             enddo
             end
             """, ")"
    )

    fun testType() = doMatch("""
            <caret>program a
            type (met_data), intent(inout) :: fg_data

            integer :: i
            do i=1,4
                write(*,*) i
            enddo
            end""", "end")

    fun testTypeDecl() = doMatch("""
            program a
            <caret>type :: fg_data
                integer :: a
            endtype fg_data
            integer :: i
            do i=1,4
                write(*,*) i
            enddo
            end""", "endtype")

    fun testProgram() = doMatch("""
             <caret>program a
             integer :: i
             do i=1,4
                write(*,*) i
             enddo
             end
             """, "end"
    )

    fun testDo() = doMatch("""
             program a
             integer :: i
             <caret>do i=1,4
                write(*,*) i
             enddo
             end
             """, "enddo"
    )

    fun testLabeledDo() = doMatch("""
             program a
             integer :: i
             <caret>do 10 i=1,4
                write(*,*) i
          10 enddo
             end
             """, "enddo"
    )

    fun testIf() = doMatch("""
             program a
             integer :: i
             if (2 > 1) <caret>then
                 if (3 > 2) write(*,*) "a"
             else if (3>2) then
                 write(*,*) "aa"
             endif
             end
             """, "else"
    )

    fun testForall() = doMatch("""
             program a
             integer :: i
             <caret>forall (i=1:3)
             endforall
             end
             """, "endforall"
    )

    fun testSubroutineWithSelectType() = doMatch("""
    <caret>subroutine a()
        integer :: i

        select type(i)
        type is (integer)
            write(*,*) "integer"
        type is (real(4))
            write(*,*) "real(4)"
        class default
            write(*,*) "unknown"
        end select
    endsubroutine
    """, "endsubroutine")

    fun testSelectType() = doMatch("""
    subroutine a()
        integer :: i

        <caret>select type(i)
        type is (integer)
            write(*,*) "integer"
        type is (real(4))
            write(*,*) "real(4)"
        class default
            write(*,*) "unknown"
        endselect
    endsubroutine
    """, "endselect")

    private fun doMatch(@Language("Fortran") source: String, coBrace: String) {
        myFixture.configureByText(FortranFileType, source)
        assertEquals(
                getMatchedBraceOffset(myFixture.editor, true, myFixture.file),
                source.replace("<caret>", "").lastIndexOf(coBrace))
    }
}