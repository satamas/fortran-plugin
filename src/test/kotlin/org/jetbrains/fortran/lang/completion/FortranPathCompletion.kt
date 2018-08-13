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
}