package org.jetbrains.fortran.lang.completion

import org.junit.Test

class FortranPathCompletion : FortranCompletionTestBase() {
    @Test
    fun `test use only`() = doSingleCompletion(
        """
    use TestModule, only : foo
    bar = fo<caret>
    end
    """,
        """
    use TestModule, only : foo
    bar = foo<caret>
    end
    """)

    @Test
    fun `test use only with rename`() = doSingleCompletion("""
    use TestModule, only : foo => fo
    bar = f<caret>
    end
    """,
        """
    use TestModule, only : foo => fo
    bar = foo<caret>
    end
    """)
}