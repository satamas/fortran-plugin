package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranObsoleteOperatorInspectionTest() :
    org.jetbrains.fortran.ide.inspections.FortranInspectionsBaseTestCase(org.jetbrains.fortran.ide.inspections.FortranObsoleteOperatorInspection()) {
    @Test
    fun testUppercaseObsoleteOperator() = checkFixByText(
        "Obsolete operator fix", """program a
    if (1 <warning descr="Obsolete operator">.GE.<caret></warning> 2) write(*,*) "1>=2"
    end
    """, """program a
    if (1 >= 2) write(*,*) "1>=2"
    end
    """, true)

    @Test
    fun testLowercaseObsoleteOperator() = checkFixByText("Obsolete operator fix","""program a
    if (1 <warning descr="Obsolete operator">.lt.<caret></warning> 2) write(*,*) "1<2"
    end
    """, """program a
    if (1 < 2) write(*,*) "1<2"
    end
    """, true)

    @Test
    fun testObsoleteOperatorWithoutSpacesAroundIt() = checkFixByText("Obsolete operator fix","""program a
    if (1<warning descr="Obsolete operator">.eq.<caret></warning>2) write(*,*) "1=2"
    end
    """, """program a
    if (1==2) write(*,*) "1=2"
    end
    """, true)

    @Test
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