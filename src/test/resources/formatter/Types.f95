module types
    interface inter


        function f(n);
                        real(8) :: f
                integer n
        end function f















        subroutine s(n)
                integer :: n
                        end subroutine s
    end interface

    type :: person
        character(len = 20) :: forename
                integer :: age
                integer :: some
    end type person
endmodule