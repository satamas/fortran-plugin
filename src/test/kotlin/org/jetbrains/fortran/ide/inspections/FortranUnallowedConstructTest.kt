package org.jetbrains.fortran.ide.inspections

class FortranUnallowedConstructTest()
    : FortranInspectionsBaseTestCase(FortranUnallowedConstructInspection()) {

    fun testStmtsInForallConstruct() = checkByText("""
        forall (i=1:10:2)
            <error descr="This construct is not allowed in forall construct">do j=1,3
            enddo</error>
        endforall
        end
    """)

    fun testStmtsInWhereConstruct() = checkByText("""
        where (1<2)
            <error descr="This construct is not allowed in where construct">forall (j=1:2)
            endforall</error>
        endwhere
        end
    """)

}