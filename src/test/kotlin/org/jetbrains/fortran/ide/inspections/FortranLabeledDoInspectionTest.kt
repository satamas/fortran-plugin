package org.jetbrains.fortran.ide.inspections

class FortranLabeledDoInspectionTest()
    : FortranInspectionsBaseTestCase(FortranLabeledDoInspection()) {

    fun testEndDo() = checkFixByText("Convert to Nonlabaled do construct","""program a
    do <warning descr="Labeled do construct is deprecated">1<caret></warning> j = 1, 3
    1 end do
    end
    """, """program a
    do j = 1, 3
    end do
    end
    """, true)

    fun testUsedLabel() = checkFixByText("Convert to Nonlabaled do construct","""program a
    do <warning descr="Labeled do construct is deprecated">1<caret></warning> j = 1, 3
    1 end do
    goto 1
    end
    """, """program a
    do j = 1, 3
    1 end do
    goto 1
    end
    """, true)

    fun testTermAction() = checkFixByText("Convert to Nonlabaled do construct","""program a
    do <warning descr="Labeled do construct is deprecated">1<caret></warning> j = 1, 3
        1 a = 1
    end
    """, """program a
    do j = 1, 3
        a = 1
    end do
    end
    """, true)

    fun testTermConstruct() = checkFixByText("Convert to Nonlabaled do construct","""program a
    do <warning descr="Labeled do construct is deprecated">1<caret></warning> j = 1, 3
        do 1 i = 1,4

        1 end do
    end
    """, """program a
    do j = 1, 3
        do 1 i = 1,4

        1 end do
    end do
    end
    """, true)
}