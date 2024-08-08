module mod
    type :: dot
        integer :: x, y, z
        contains
                procedure:: sub => sub1
    end type dot

    type :: cut
        type(dot) :: a, b
    end type cut

    type :: cutarray
        type(cut), dimension(10) :: dat
    end type cutarray

contains

    real function l(o)
        type :: object
            integer :: id
                    type(cutarray) :: ar

        end type object

        type(object) :: o
        call t1%sub()
        return o%ar%dat(5)%b%<caret>x
    end function
end module mod