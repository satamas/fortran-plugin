module types<fold text='...'>
    type :: person<fold text='...'>
        character(len=20) :: forename
        integer :: age
    </fold>end type person

    interface solve<fold text='...'>
        function solve(n, pmatrix, key)<fold text='...'>
            real*8, dimension (:), pointer :: solve;
        </fold>end function solve
    </fold>end interface

    enum, bind(c)<fold text='...'>
        enumerator :: red = 4, blue = 9
        enumerator yellow
    </fold>end enum
</fold>end module