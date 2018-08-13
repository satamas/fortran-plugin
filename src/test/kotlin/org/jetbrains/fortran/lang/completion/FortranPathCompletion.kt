package org.jetbrains.fortran.lang.completion

class FortranPathCompletion : FortranCompletionTestBase() {
    fun `test use only`() = doSingleCompletion("""
    use TestModule, only : foo
    bar = fo<caret>
    end
    """,
            """
    use TestModule, only : foo
    bar = foo<caret>
    end
    """)

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