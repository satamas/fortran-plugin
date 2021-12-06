package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranUnusedConstructNameTest() : FortranInspectionsBaseTestCase(FortranUnusedConstructNameInspection()) {
    @Test
    fun testUnusedConstructName() = checkByText(
        """
        <weak_warning descr="Unused construct name">myDo:</weak_warning> do i=1,2
        enddo
        end
    """, true, false, true
    )

    @Test
    fun testUnusedConstructNameFix() = checkFixByText("Delete construct name","""
        <weak_warning descr="Unused construct name"><caret>myDo:</weak_warning> do i=1,2
        enddo
        end
    """, """
        do i=1,2
        enddo
        end
    """, true, false, true)

    @Test
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