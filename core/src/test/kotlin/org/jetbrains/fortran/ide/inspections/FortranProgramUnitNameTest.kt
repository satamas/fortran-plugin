package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranProgramUnitNameTest() :
    org.jetbrains.fortran.ide.inspections.FortranInspectionsBaseTestCase(FortranProgramUnitNameMismatchInspection()) {
    @Test
    fun testIfConstruct() = checkByText(
        """
        program myProgram
            write(*,*) myFunction()
        contains
            function myFunction()
                myFunction = 1
            end function myFunction
        end program <error descr="Program unit name mismatch">myFunction</error>
    """)

    @Test
    fun testFix() = checkFixByText("Fix unit name", """
        program myProgram
            write(*,*) "!"
        end program <error descr="Program unit name mismatch">myFunction<caret></error>
        """, """
        program myProgram
            write(*,*) "!"
        end program myProgram
        """)
}