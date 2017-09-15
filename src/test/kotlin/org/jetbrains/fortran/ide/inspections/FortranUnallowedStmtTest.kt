package org.jetbrains.fortran.ide.inspections

class FortranUnallowedStmtTest()
    : FortranInspectionsBaseTestCase(FortranUnallowedStmtInspection()) {

    fun testStmtsInForallConstruct() = checkByText("""
        forall (i=1:10:2)
            <error descr="This statement is not allowed in forall construct">write(*,*) i</error>
        endforall
        end
    """)

    fun testStmtsInWhereConstruct() = checkByText("""
        where (1<2)
            <error descr="This statement is not allowed in where construct">write(*,*) i</error>
        endwhere
        end
    """)

    fun testSpecificationStmtsInConstruct() = checkByText("""
        do i=1,3
            <error descr="Specification statement is not allowed here">integer :: i</error>
            <error descr="Specification statement is not allowed here">allocatable :: i</error>
        enddo
        end
    """)

}