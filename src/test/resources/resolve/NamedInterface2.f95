module m
    interface pr
    subroutine pri(i)
    integer :: i
end subroutine

subroutine pr(x)
    real(8) :: x
end subroutine
end interface pr
end module m