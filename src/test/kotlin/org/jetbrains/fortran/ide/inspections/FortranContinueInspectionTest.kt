package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranContinueInspectionTest() : FortranInspectionsBaseTestCase(FortranContinueInspection()) {

    @Test
    fun testContinue() = checkFixByText(
        "Continue statement without label fix", """program a
    b=1
    <warning descr="Continue statement without label">continue<caret></warning>
    end
    """, """program a
    b=1
    end
    """, true)

    @Test
    fun testContinueWithOtherStmts() = checkFixByText("Continue statement without label fix","""program a
    b=1
    <warning descr="Continue statement without label">continue<caret></warning>
    c=1
    end
    """, """program a
    b=1
    c=1
    end
    """, true)

    @Test
    fun testContinueWithEmptyLines() = checkFixByText("Continue statement without label fix","""program a
    b = 1

    <warning descr="Continue statement without label">continue<caret></warning>

    end
    """, """program a
    b = 1


    end
    """, true)

    @Test
    fun testContinueWithComment() = checkFixByText("Continue statement without label fix","""program a
    <warning descr="Continue statement without label">continue<caret></warning> ! comment
    end
    """, """program a
    ! comment
    end
    """, true)

}