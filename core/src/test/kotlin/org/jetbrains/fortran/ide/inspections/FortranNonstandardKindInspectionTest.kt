package org.jetbrains.fortran.ide.inspections

import org.junit.Test

class FortranNonstandardKindInspectionTest() :
    org.jetbrains.fortran.ide.inspections.FortranInspectionsBaseTestCase(FortranNonstandardKindInspection()) {

    @Test
    fun testReal8() = checkFixByText(
        "Nonstandard Kind Selector fix", """program prog1
        real<warning descr="Nonstandard Kind Selector">*8<caret></warning> :: a
    end
    """, """program prog1
        real(kind=8) :: a
    end
    """, true)

    @Test
    fun testComplex16() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        complex<warning descr="Nonstandard Kind Selector">*16<caret></warning> :: a
    end
    """, """program prog1
        complex(kind=8) :: a
    end
    """, true)

    @Test
    fun testRealN() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        real<warning descr="Nonstandard Kind Selector">*N<caret></warning> :: a
    end
    """, """program prog1
        real(kind=N) :: a
    end
    """, true)

    @Test
    fun testComplexN() = checkFixByText("Nonstandard Kind Selector fix","""program prog1
        complex<warning descr="Nonstandard Kind Selector">*N<caret></warning> :: a
    end
    """, """program prog1
        complex(kind=(N)/2) :: a
    end
    """, true)
}