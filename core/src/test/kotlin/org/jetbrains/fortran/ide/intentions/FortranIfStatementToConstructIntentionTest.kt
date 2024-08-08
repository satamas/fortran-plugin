package org.jetbrains.fortran.ide.intentions

import org.junit.Test

class FortranIfStatementToConstructIntentionTest :
    FortranIntentionsBaseTestCase(FortranIfStatementToConstructIntention()) {
    @Test
    fun testSimpleIfStmt() = testIntention(
        """
    program test
        if (.true.) write<caret>("AAA")
    end""", """
    program test
        if (.true.) then<caret>
            write("AAA")
        end if
    end""")

    @Test
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