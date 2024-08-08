package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranConstructNameTest() :
    org.jetbrains.fortran.ide.inspections.FortranInspectionsBaseTestCase(FortranConstructNameMismatchInspection()) {

    @Test
    fun testIfConstruct() = checkByText(
        """
        myIf: if (2 > 1) then
            write (*,*) "2>1"
        else if (2 == 1) then MYIF
            write(*,*) "2=1"
        else <error descr="Construct name mismatch">notMyIf</error>
            write(*,*) "2<1"
        endif <error descr="Construct name mismatch">if</error>

        if: if (2*2 .eq. 4) then
            write(*,*) "2*2=4"
        endif
        end
    """)

    @Test
    fun testFix() = checkFixByText("Construct name fix", """
        myDo: do i=1,5,1
            write(*,*) i
        end do <error descr="Construct name mismatch">myIf<caret></error>
        end
        """, """
        myDo: do i=1,5,1
            write(*,*) i
        end do myDo
        end
        """)
}