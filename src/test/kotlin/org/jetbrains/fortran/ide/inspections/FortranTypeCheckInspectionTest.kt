package org.jetbrains.fortran.ide.inspections

class FortranTypeCheckInspectionTest()
    : FortranInspectionsBaseTestCase(FortranTypeCheckInspection()) {

    fun testDeclarationIntegerToInteger()  = checkByText("""
        program p
        integer :: a = 1
        end program
    """, true)

    fun testDeclarationIntegerArithmBinaryOperationToInteger()  = checkByText("""
        program p
        integer :: a = 1 + 3 * 2 - 8 / 19 - 0
        end program
    """, true)

    fun testDeclarationParenthesizedIntegerToInteger()  = checkByText("""
        program p
        integer :: a = (((((1)))))
        end program
    """, true)

    fun testDeclarationCharacterToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `character`">"a"</warning>
        end program
    """, true)

    fun testDeclarationLogicalToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `logical`">.true.</warning>
        end program
    """, true)

    fun testDeclarationComplexToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `complex`">(1, 2)</warning>
        end program
    """, true)

    fun testDeclarationRealToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `real`">3.14</warning>
        end program
    """, true)

    fun testDeclarationDoublePrecisionRealToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `double precision`">1.0D0</warning>
        end program
    """, true)

    fun testDeclarationRealAdditionToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `real`">1 + 1.0</warning>
        end program
    """, true)

    fun testDeclarationLogicalAndToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched typesexpected `integer`, found `logical`">.true. .AND. .false.</warning>
        end program
    """, true)

    fun testDeclarationUnableToInferTypeToInteger() = checkByText("""
        program p
        integer :: a = <warning descr="mismatched argument typesleft argument: integer, right argument: logical">1 + .true.</warning>
        end program
    """, true)

    fun testDeclarationRealToReal() = checkByText("""
        program p
        real :: a = 1.0
        end program
    """, true)

    fun testDeclarationCharacterToReal() = checkByText("""
        program p
        real :: a = <warning descr="mismatched typesexpected `real`, found `character`">"1.0"</warning>
        end program
    """, true)

    fun testDeclarationLogicalToReal() = checkByText("""
        program p
        real :: a = <warning descr="mismatched typesexpected `real`, found `logical`">.true.</warning>
        end program
    """, true)

    fun testDeclarationComplexToReal() = checkByText("""
        program p
        real :: a = <warning descr="mismatched typesexpected `real`, found `complex`">(1.0, 2.0)</warning>
        end program
    """, true)

    fun testDeclarationIntegerToReal() = checkByText("""
        program p
        real :: a = 3
        end program
    """, true)

    fun testDeclarationDoublePrecisionRealToReal() = checkByText("""
        program p
        real :: a = <warning descr="mismatched typesexpected `real`, found `double precision`">1.0D0</warning>
        end program
    """, true)

    fun testDeclarationRealAdditionToReal() = checkByText("""
        program p
        real :: a = 1 + 1.0
        end program
    """, true)

    fun testDeclarationLogicalNotToReal() = checkByText("""
        program p
        real :: a = <warning descr="mismatched typesexpected `real`, found `logical`">.not. .true.</warning>
        end program
    """, true)

    fun testDeclarationParenthesizedRealToReal()  = checkByText("""
        program p
        real :: a = (((((1)))))
        end program
    """, true)

    fun testDeclarationUnableToInferTypeToReal() = checkByText("""
        program p
        real :: a = <warning descr="mismatched argument typesleft argument: real, right argument: character">1.1 + "0.1"</warning>
        end program
    """, true)

    fun testDeclarationLogicalToLogical() = checkByText("""
        program p
        logical :: a = .true.
        logical :: b = .false.
        end program
    """, true)

    fun testDeclarationCharacterToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `character`">"true"</warning>
        end program
    """, true)

    fun testDeclarationIntegerToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `integer`">0</warning>
        end program
    """, true)

    fun testDeclarationRealToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `real`">0.0</warning>
        end program
    """, true)

    fun testDeclarationComplexToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `complex`">(1, 0)</warning>
        end program
    """, true)

    fun testDeclarationDoublePrecisionLogicalToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `double precision`">1.0D0</warning>
        end program
    """, true)

    fun testDeclarationRealAdditionToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `real`">0 - 1.0</warning>
        end program
    """, true)

    fun testDeclarationLogicalNotToLogical() = checkByText("""
        program p
        logical :: a = .not. .false.
        end program
    """, true)

    fun testDeclarationParenthesizedIntegerToLogical()  = checkByText("""
        program p
        logical :: a = <warning descr="mismatched typesexpected `logical`, found `integer`">(((((1)))))</warning>
        end program
    """, true)

    fun testDeclarationParenthesizedLogicalToLogical()  = checkByText("""
        program p
        logical :: a = (((((.true.)))))
        end program
    """, true)

    fun testDeclarationUnableToInferTypeToLogical() = checkByText("""
        program p
        logical :: a = <warning descr="mismatched argument typesleft argument: logical, right argument: character">.true. .OR. "false"</warning>
        end program
    """, true)


    fun testDeclarationComplexToComplex() = checkByText("""
        program p
        complex :: a = (1.0, 2)
        end program
    """, true)

    fun testDeclarationCharacterToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `character`">"(1, 2.0)"</warning>
        end program
    """, true)

    fun testDeclarationIntegerToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `integer`">0</warning>
        end program
    """, true)

    fun testDeclarationRealToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `real`">0.0</warning>
        end program
    """, true)

    fun testDeclarationDoublePrecisionToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `double precision`">1.0D0</warning>
        end program
    """, true)

    fun testDeclarationRealAdditionToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `real`">0 - 1.0</warning>
        end program
    """, true)

    fun testDeclarationComplexMultiplicationToComplex() = checkByText("""
        program p
        complex :: a = (0.1, 8.9) * (1.0, 2)
        end program
    """, true)

    fun testDeclarationLogicalOrToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `logical`">.true. .OR. (.not. .false.)</warning>
        end program
    """, true)

    fun testDeclarationUnableToInferTypeToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched argument typesleft argument: complex, right argument: character">(1.0, 2) + "(0.1, 0.2)"</warning>
        end program
    """, true)


    fun testDeclarationCharacterToCharacter() = checkByText("""
        program p
        character :: a = "a"
        end program
    """, true)

    fun testDeclarationComplexToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `complex`">(1, 2.0)</warning>
        end program
    """, true)

    fun testDeclarationIntegerToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `integer`">0</warning>
        end program
    """, true)

    fun testDeclarationRealToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `real`">0.0</warning>
        end program
    """, true)

    fun testDeclarationDoublePrecisionToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `double precision`">1.0D0</warning>
        end program
    """, true)

    fun testDeclarationRealPowerToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `real`">2.1 ** 1</warning>
        end program
    """, true)

    fun testDeclarationComplexMultiplicationToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `complex`">(0.1, 8.9) * (1.0, 2)</warning>
        end program
    """, true)

    fun testDeclarationLogicalOrToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched typesexpected `character`, found `logical`">.true. .OR. (.not. .false.)</warning>
        end program
    """, true)

    fun testDeclarationConcatToCharacter() = checkByText("""
        program p
        character :: a = "a"
        character :: b = "b"
        character :: ab = a // b
        end program
    """, true)

    fun testDeclarationUnableToInferTypeToCharacter() = checkByText("""
        program p
        character :: a = <warning descr="mismatched argument typesleft argument: character, right argument: complex">"(1.0, 2)" // (0.1, 0.2)</warning>
        end program
    """, true)

    fun testAssignmentIntegerToInteger()  = checkByText("""
        program p
        integer :: a = 1
        a = -1
        end program
    """, true)

    fun testAssignmentIntegerArithmBinaryOperationToInteger()  = checkByText("""
        program p
        integer :: a = 1
        a = a + 3 * 2 - 8 / 19 - 0
        end program
    """, true)

    fun testAssignmentParenthesizedIntegerToInteger()  = checkByText("""
        program p
        integer :: a = 1
        a = (((((1)))))
        end program
    """, true)

    fun testAssignmentLogicalToInteger() = checkByText("""
        program p
        integer :: a = 1
        a = <warning descr="mismatched typesexpected `integer`, found `logical`">.true.</warning>
        end program
    """, true)

    fun testAssignmentRealToInteger() = checkByText("""
        program p
        integer :: a = 2
        a = <warning descr="mismatched typesexpected `integer`, found `real`">3.14</warning>
        end program
    """, true)

    fun testAssignmentRealAdditionToInteger() = checkByText("""
        program p
        integer :: a = 1
        a = <warning descr="mismatched typesexpected `integer`, found `real`">1 + 1.0</warning>
        end program
    """, true)

    fun testAssignmentRealToReal() = checkByText("""
        program p
        real :: a
        a = 1.1
        a = -1.2
        a = 4.5
        end program
    """, true)

    fun testAssignmentCharacterToReal() = checkByText("""
        program p
        real :: a = 2
        a = <warning descr="mismatched typesexpected `real`, found `character`">"1.0"</warning>
        end program
    """, true)

    fun testAssignmentIntegerToReal() = checkByText("""
        program p
        real :: a = 3.14
        a = 3
        end program
    """, true)

    fun testAssignmentLogicalNotToReal() = checkByText("""
        program p
        real :: a = 2
        a = <warning descr="mismatched typesexpected `real`, found `logical`">.not. .true.</warning>
        end program
    """, true)

    fun testAssignmentLogicalToLogical() = checkByText("""
        program p
        logical :: a
        logical :: b
        a = .true.
        b = .false.
        end program
    """, true)

    fun testAssignmentIntegerToLogical() = checkByText("""
        program p
        logical :: a = .true.
        a = <warning descr="mismatched typesexpected `logical`, found `integer`">0</warning>
        end program
    """, true)

    fun testAssignmentComplexToLogical() = checkByText("""
        program p
        logical :: a
        a = .true.
        a = <warning descr="mismatched typesexpected `logical`, found `complex`">(1, 0)</warning>
        a = .false.
        end program
    """, true)

    fun testAssignmentLogicalNotToLogical() = checkByText("""
        program p
        logical :: a
        a = .not. .false.
        end program
    """, true)

    fun testAssignmentComplexToComplex() = checkByText("""
        program p
        complex :: a
        a = (1.0, 2)
        end program
    """, true)

    fun testAssignmentRealToComplex() = checkByText("""
        program p
        complex :: a = (1, 2)
        a = <warning descr="mismatched typesexpected `complex`, found `real`">0.0</warning>
        end program
    """, true)

    fun testAssignmentDoublePrecisionToComplex() = checkByText("""
        program p
        complex :: a = <warning descr="mismatched typesexpected `complex`, found `double precision`">1.0D0</warning>
        end program
    """, true)

    fun testAssignmentCharacterToCharacter() = checkByText("""
        program p
        character :: a
        a = "a"
        end program
    """, true)

    fun testAssignmentComplexToCharacter() = checkByText("""
        program p
        character :: a = "a"
        a = "b"
        a = <warning descr="mismatched typesexpected `character`, found `complex`">(1, 2.0)</warning>
        end program
    """, true)

    fun testAssignmentConcatToCharacter() = checkByText("""
        program p
        character :: a = "a"
        character :: b = "b"
        character :: ab
        ab = a // b
        end program
    """, true)

    fun testDeclarationDoublePrecisionToDoublePrecision() = checkByText("""
        program p
        double precision :: a = 0.25D0
        end program
    """, true)

    fun testDeclarationCharacterToDoublePrecision() = checkByText("""
        program p
        double precision :: a = <warning descr="mismatched typesexpected `double precision`, found `character`">"1.0D0"</warning>
        end program
    """, true)

    fun testDeclarationLogicalToDoublePrecision() = checkByText("""
        program p
        double precision :: a = <warning descr="mismatched typesexpected `double precision`, found `logical`">.true.</warning>
        end program
    """, true)

    fun testDeclarationComplexToDoublePrecision() = checkByText("""
        program p
        double precision :: a = <warning descr="mismatched typesexpected `double precision`, found `complex`">(1.0D0, 2.0D0)</warning>
        end program
    """, true)

    fun testDeclarationIntegerToDoublePrecision() = checkByText("""
        program p
        double precision :: a = 3
        end program
    """, true)

    fun testDeclarationRealToDoublePrecision() = checkByText("""
        program p
        double precision :: a = 1.0
        end program
    """, true)

    fun testDeclarationDoublePrecisionAdditionToDoublePrecision() = checkByText("""
        program p
        double precision :: a = 1 + 1.0 + 1.0D0
        end program
    """, true)

    fun testDeclarationLogicalNotToDoublePrecision() = checkByText("""
        program p
        double precision :: a = <warning descr="mismatched typesexpected `double precision`, found `logical`">.not. .true.</warning>
        end program
    """, true)

    fun testDeclarationParenthesizedDoublePrecisionToDoublePrecision()  = checkByText("""
        program p
        double precision :: a = (((((1.24D0)))))
        end program
    """, true)

    fun testDeclarationUnableToInferTypeToDoublePrecision() = checkByText("""
        program p
        double precision :: a = <warning descr="mismatched argument typesleft argument: double precision, right argument: character">1.1D0 + "0.1"</warning>
        end program
    """, true)

    fun testDeclarationIntegerArray() = checkByText("""
        program p
        integer, dimension(3) :: a = (/1, 2, 3/)
        end program
    """, true)

    fun testAssignmentIntegerArray() = checkByText("""
        program p
        integer, dimension(3) :: a
        a = (/1, 2, 3/)
        end program
    """, true)

    fun testDeclarationRealToIntegerArray() = checkByText("""
        program p
        integer, dimension(3) :: a
        a = <warning descr="mismatched typesexpected `integer array of shape (1:3)`, found `real array of shape (1:3)`">(/1, 2, 3.1/)</warning>
        end program
    """, true)

    fun testAssignmentIntegerToRealArray() = checkByText("""
        program p
        real, dimension(3) :: a
        a = (/1, 2, 3/)
        end program
    """, true)

    fun testAssignmentMalformedArrayConstructorToCharacterArray() = checkByText("""
        program p
        character, dimension(3) :: a
        a = (/"a", <warning descr="mismatched array constructor element typearray base type: character, element type: integer">2</warning>, "c"/)
        end program
    """, true)

    fun testAssignmentCorrectImplicitDo() = checkByText("""
        program p
        integer, dimension(3) :: a
        a = (/(I, I = 4, 6)/)
        end program
    """, true)

    fun testAssignmentWrongArraySize() = checkByText("""
        program p
        logical, dimension(3) :: a
        a = <warning descr="mismatched typesexpected `logical array of shape (1:3)`, found `logical array of shape (1:2)`">(/.true., .false./)</warning>
        end program
    """, true)

    fun testDoublePrecisionArray() = checkByText("""
        program p
        double precision, dimension(3) :: a
        a = (/1, 1.0, 1.0D0/)
        a = <warning descr="mismatched typesexpected `double precision array of shape (1:3)`, found `logical array of shape (1:3)`">(/.true., .false., .true./)</warning>
        end program
    """, true)

    // TODO incorrect implicit do test

}