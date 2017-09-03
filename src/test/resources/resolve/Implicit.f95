program implicit
    X = 1
    x = x + 1
    x = X + x + 1
    x = x + x + x + 1
    x = <caret>x + 1
    call print
contains
    subroutine pr()
        write(*,*) X*x
    end subroutine
end

subroutine print()
    x = 1
    write(*,*) X, x+1
end subroutine