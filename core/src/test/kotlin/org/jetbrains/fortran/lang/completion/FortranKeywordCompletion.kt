package org.jetbrains.fortran.lang.completion

import org.junit.Test

class FortranKeywordCompletion : FortranCompletionTestBase() {
    @Test
    fun `test keyword`() = doSingleCompletion(
        """
    module test
    inc<caret>
    end module test
    """,
        """
    module test
    include<caret>
    end module test
    """)

    @Test
    fun `test inside block data`() = doSingleCompletion("""
    block data test
    equ<caret>
    end block data test
    """,
        """
    block data test
    equivalence<caret>
    end block data test
    """)

    @Test
    fun `test type inside block data`() = doSingleCompletion("""
    block data test
    int<caret>
    end block data test
    """,
        """
    block data test
    integer<caret>
    end block data test
    """)

    @Test
    fun `test action statement inside block data`() = checkNoCompletion("""
    block data test
    cycl<caret>
    end block data test
    """)
}