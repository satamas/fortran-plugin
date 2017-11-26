package org.jetbrains.fortran.ide.inspections

class FortranObsoleteOperatorInspectionTest()
    : FortranInspectionsBaseTestCase(FortranObsoleteOperatorInspection()) {

    fun testUppercaseObsoleteOperator() = checkFixByText("Obsolete operator fix","""program a
    if (1 <warning descr="Obsolete operator">.GE.<caret></warning> 2) write(*,*) "1>=2"
    end
    """, """program a
    if (1 >= 2) write(*,*) "1>=2"
    end
    """, true)

    fun testLowercaseObsoleteOperator() = checkFixByText("Obsolete operator fix","""program a
    if (1 <warning descr="Obsolete operator">.lt.<caret></warning> 2) write(*,*) "1<2"
    end
    """, """program a
    if (1 < 2) write(*,*) "1<2"
    end
    """, true)

    fun testObsoleteOperatorWithoutSpacesAroundIt() = checkFixByText("Obsolete operator fix","""program a
    if (1<warning descr="Obsolete operator">.eq.<caret></warning>2) write(*,*) "1=2"
    end
    """, """program a
    if (1==2) write(*,*) "1=2"
    end
    """, true)

    fun testObsoleteOperatorWithComments() = checkFixByText("Obsolete operator fix","""program a
    if (1 &
    ! Comment
    <warning descr="Obsolete operator">.ne.<caret></warning> &
    ! Comment
    2) write(*,*) "1!=2"
    end
    """, """program a
    if (1 &
    ! Comment
    /= &
    ! Comment
    2) write(*,*) "1!=2"
    end
    """, true)
}