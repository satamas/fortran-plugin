package org.jetbrains.fortran.ide.inspections

class FortranUnusedLabelTest()
    : FortranInspectionsBaseTestCase(FortranUnusedLabelInspection()) {

    fun testUnusedLabel() = checkByText("""
        <weak_warning descr="Unused label declaration">1</weak_warning> continue
        end
    """, true, false, true)

    fun testUnusedLabelFix() = checkFixByText("Delete label declaration","""
        <weak_warning descr="Unused label declaration">1<caret></weak_warning> continue
        end
    """, """
         continue
        end
    """, true, false, true)
}