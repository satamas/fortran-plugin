package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranUnusedLabelTest() : FortranInspectionsBaseTestCase(FortranUnusedLabelInspection()) {
    @Test
    fun testUnusedLabel() = checkByText(
        """
        <weak_warning descr="Unused label declaration">1</weak_warning> continue
        end
    """, true, false, true
    )

    @Test
    fun testUnusedLabelFix() = checkFixByText("Delete label declaration","""
        <weak_warning descr="Unused label declaration">1<caret></weak_warning> continue
        end
    """, """
        continue
        end
    """, true, false, true)
}