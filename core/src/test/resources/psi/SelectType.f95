subroutine a()
    integer :: i

    select type(i)
    type is (integer)
        write(*,*) "integer"
    type is (real(4))
        write(*,*) "real(4)"
    class default
        write(*,*) "unknown"
    end select
end subroutine
