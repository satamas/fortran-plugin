module test_mod

implicit none
double complex :: var1
doublecomplex :: var2
type ty1 !define a class including a procedure
    contains
        procedure:: sub => sub1
end type

contains

subroutine sub1(this)
    class(ty1) :: this
    print*,"sub1"
end subroutine

subroutine test1()
type(ty1):: t1
call t1%sub() !call class procedure; parser error: '%' or '[' expected, got ''
end subroutine

end module