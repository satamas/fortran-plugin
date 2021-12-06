package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranUnresolvedLabelTest() : FortranInspectionsBaseTestCase(FortranUnresolvedLabelInspection()) {
    @Test
    fun testUnresolvedLabelInGoTo() = checkByText(
        """
        goto <error descr="Unresolved label">1</error>
        end
    """
    )

    @Test
    fun testUnresolvedLabelInErrParameter() = checkByText("""
        write(*,1,err=<error descr="Unresolved label">2</error>) 3.1415927, 3.1415927
        1  format (6x, e15.7, 3x, e6.3)
        end
    """)

    @Test
    fun testUnresolvedLabelInUnits() = checkByText("""
        function a()
            1 a = 1
        end function a

        function b()
            goto <error descr="Unresolved label">1</error>
        end function b
    """)
}