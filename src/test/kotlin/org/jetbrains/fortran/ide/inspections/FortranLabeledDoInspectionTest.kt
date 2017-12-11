package org.jetbrains.fortran.ide.inspections

class FortranLabeledDoInspectionTest()
    : FortranInspectionsBaseTestCase(FortranLabeledDoInspection()) {

    fun testEndDo() = checkFixByText("Convert to Nonlabaled do construct","""program a
    <warning descr="Labeled do construct is deprecated">do 1<caret> j = 1, 3</warning>
    1 end do
    end
    """, """program a
    do j = 1, 3
    end do
    end
    """, true)

    fun testUsedLabel() = checkFixByText("Convert to Nonlabaled do construct","""program a
    <warning descr="Labeled do construct is deprecated">do 1 j = 1, 3<caret></warning>
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
    <warning descr="Labeled do construct is deprecated">do 1<caret>j = 1, 3</warning>
        1 a = 1
    end
    """, """program a
    do j = 1, 3
        a = 1
    end do
    end
    """, true)

    fun testContinue() = checkFixByText("Convert to Nonlabaled do construct","""program a
    <warning descr="Labeled do construct is deprecated">do 1<caret>j = 1, 3</warning>
        a = 1
        1 continue
    end
    """, """program a
    do j = 1, 3
        a = 1
    end do
    end
    """, true)

    fun testTermConstruct() = checkFixByText("Convert to Nonlabaled do construct","""program a
    <warning descr="Labeled do construct is deprecated">do 1<caret> j = 1, 3</warning>
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