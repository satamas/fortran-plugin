program myLittleProgram
    integer a
    allocatable :: A
    asynchronous :: A
    CODIMENSION :: A[:]
    CONTIGUOUS A
    DIMENSION :: A(1:2,3:4)
    INTENT (INOUT) :: A
    OPTIONAL :: A
    POINTER :: A
    PROTECTED :: B, A
    VALUE A
    VOLATILE <caret>A
end