program a
    use m
    implicit none
    integer :: i = 1
    real(8) :: x = 1

    call pr(i)
    call pr(x)

end program



subroutine pri(i)
    integer :: i
    write(*,*) i
end subroutine

subroutine pr(x)
    real(8) :: x
    write(*,*) x+1
end subroutine