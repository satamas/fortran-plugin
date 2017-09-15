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

    fun testStmtsInInterfaces() = checkByText("""
      module gaussinter
      interface solve
        function solve(n, pmatrix, key);
              real*8, dimension (:), pointer ::  solve;
              real(8), dimension (:,:), pointer :: pmatrix;
              integer n, key
              <error descr="This statement is not allowed inside the interface">n = n + 1</error>
        end function solve
      end interface
      end module
    """)

    fun testStmtsInModule() = checkByText("""
      module gaussinter
      integer :: a
      <error descr="This statement is not allowed in this program unit">a = 10</error>
      end module
    """)


    fun testSpecificationStmtsInConstruct() = checkByText("""
        do i=1,3
            <error descr="Specification statement is not allowed here">integer :: i</error>
            <error descr="Specification statement is not allowed here">allocatable :: i</error>
        enddo
        end
    """)

}