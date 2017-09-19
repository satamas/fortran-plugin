package org.jetbrains.fortran.ide.inspections

class FortranUnusedConstructNameTest()
    : FortranInspectionsBaseTestCase(FortranUnusedConstructNameInspection()) {

    fun testUnusedConstructName() = checkByText("""
        <weak_warning descr="Unused construct name">myDo:</weak_warning> do i=1,2
        enddo
        end
    """, true, false, true)

    fun testUnusedConstructNameFix() = checkFixByText("Delete construct name","""
        <weak_warning descr="Unused construct name"><caret>myDo:</weak_warning> do i=1,2
        enddo
        end
    """, """
        do i=1,2
        enddo
        end
    """, true, false, true)

    fun testUnusedConstructNameFixWithoutWhitesace() = checkFixByText("Delete construct name","""
        <weak_warning descr="Unused construct name"><caret>myDo:</weak_warning>do i=1,2
        enddo
        end
    """, """
        do i=1,2
        enddo
        end
    """, true, false, true)
}