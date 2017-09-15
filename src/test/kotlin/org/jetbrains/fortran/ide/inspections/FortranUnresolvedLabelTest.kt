package org.jetbrains.fortran.ide.inspections

class FortranUnresolvedLabelTest()
    : FortranInspectionsBaseTestCase(FortranUnresolvedLabelInspection()) {

    fun testUnresolvedLabelInGoTo() = checkByText("""
        goto <error descr="Unresolved label">1</error>
        end
    """)

    fun testUnresolvedLabelInErrParameter() = checkByText("""
        write(*,1,err=<error descr="Unresolved label">2</error>) 3.1415927, 3.1415927
        1  format (6x, e15.7, 3x, e6.3)
        end
    """)

    fun testUnresolvedLabelInUnits() = checkByText("""
        function a()
            1 a = 1
        end function a

        function b()
            goto <error descr="Unresolved label">1</error>
        end function b
    """)
}