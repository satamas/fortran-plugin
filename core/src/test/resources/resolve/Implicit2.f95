program implicit
    call pr()
    call print()
contains
    subroutine pr()
        x = 2
        write(*,*) X*x
    end subroutine

    subroutine print()
        x = 1
        write(*,*) <caret>X, x+1
    end subroutine
end

