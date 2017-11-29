package org.jetbrains.fortran.ide.inspections

class FortranNonstandardKindInspectionTest()
    : FortranInspectionsBaseTestCase(FortranNonstandardKindInspection()) {

    fun testReal8() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        real<warning descr="Nonstandard Kind Selector">*8<caret></warning> :: a
    end
    """, """program prog1
        real(kind=8) :: a
    end
    """, true)

    fun testComplex16() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        complex<warning descr="Nonstandard Kind Selector">*16<caret></warning> :: a
    end
    """, """program prog1
        complex(kind=8) :: a
    end
    """, true)

    fun testRealN() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        real<warning descr="Nonstandard Kind Selector">*N<caret></warning> :: a
    end
    """, """program prog1
        real(kind=N) :: a
    end
    """, true)

    fun testComplexN() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        complex<warning descr="Nonstandard Kind Selector">*N<caret></warning> :: a
    end
    """, """program prog1
        complex(kind=(N)/2) :: a
    end
    """, true)
}