package org.jetbrains.fortran.ide.inspections

class FortranUnallowedConstructTest()
    : FortranInspectionsBaseTestCase(FortranUnallowedConstructInspection()) {

    fun testConstructInForallConstruct() = checkByText("""
        forall (i=1:10:2)
            <error descr="This construct is not allowed in forall construct">do j=1,3
            enddo</error>
        endforall
        end
    """)

    fun testConstructInWhereConstruct() = checkByText("""
        where (1<2)
            <error descr="This construct is not allowed in where construct">forall (j=1:2)
            endforall</error>
        endwhere
        end
    """)

    fun testConstructInModule() = checkByText("""
        module myModule
        <error descr="Executable construct is not allowed in module">forall (j=1:2)
        endforall</error>
        end module
    """)

    fun testConstructInSubModule() = checkByText("""
        submodule (moduleName) mySubModule
        <error descr="Executable construct is not allowed in submodule">forall (j=1:2)
        endforall</error>
        end submodule
    """)

    fun testConstructInBlockData() = checkByText("""
        block data myBlock
        <error descr="Executable construct is not allowed in blockdata">forall (j=1:2)
        endforall</error>
        end block data
    """)

    fun testDeclarationConstructInConstruct() = checkByText("""
        do i=1,3
            <error descr="Declaration construct is not allowed in executable construct">type :: myType
                integer :: a
            end type myType</error>
        enddo
        end
    """)
}