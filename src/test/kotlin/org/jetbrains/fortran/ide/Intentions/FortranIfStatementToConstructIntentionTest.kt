package org.jetbrains.fortran.ide.Intentions

import org.jetbrains.fortran.ide.intentions.FortranIfStatementToConstructIntention

class FortranIfStatementToConstructIntentionTest : FortranIntentionsBaseTestCase(FortranIfStatementToConstructIntention()) {
    fun testSimpleIfStmt() = testIntention("""
    program test
        if (.true.) write<caret>("AAA")
    end""", """
    program test
        if (.true.) then<caret>
            write("AAA")
        end if
    end""")

    fun testLongIfStmt() = testIntention("""
    program test
        if (.true. &
                .and. .true.) write<caret>("AAA")
    end""", """
    program test
        if (.true. &
                .and. .true.) then<caret>
            write("AAA")
        end if
    end""")
}