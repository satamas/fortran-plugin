module TestModule

    type :: dot
        integer :: x, y, z
    end type dot

    type :: cut
        type(dot) :: a, b
    end type cut

    real(8) :: PI = 3.1415926

contains

    function length(a)
        real(8) :: length
        type(cut) :: a
        length = sqrt((a%a%x-a%b%x)**2 + (a%a%y-a%A%Y)**2 + (a%A%z-A%b%z)**2)
    end function length

end module