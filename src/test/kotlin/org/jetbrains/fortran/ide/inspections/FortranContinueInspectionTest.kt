package org.jetbrains.fortran.ide.inspections

class FortranContinueInspectionTest()
    : FortranInspectionsBaseTestCase(FortranContinueInspection()) {

    fun testContinue() = checkFixByText("Continue statement without label fix","""program a
    <warning descr="Continue statement without label">continue<caret></warning> ! comment
    end
    """, """program a
    ! comment
    end
    """, true)

}