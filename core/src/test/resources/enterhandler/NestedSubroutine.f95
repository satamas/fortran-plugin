module test
contains

    subroutine sub1 ()

    contains

        subroutine inner ()<caret>

        end subroutine inner
    end subroutine sub1
end module test