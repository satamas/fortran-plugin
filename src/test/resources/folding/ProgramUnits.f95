module circle<fold text='...'>
    real, parameter :: pi = 3.1415927
    real :: radius
</fold>contains
    subroutine input(x, y, z)<fold text='...'>
        x = y + z
    </fold>endsubroutine input
end module circle

real function average(x,y,z)<fold text='...'>
    sum = x + y + z
    average = sum /3.0
    entry average2(x, y)
    return
</fold>end

integer function zero()<fold text='...'>
    zero = 0
</fold>endfunction

subroutine input(x, y, z)<fold text='...'>
    x = y + z
</fold>end

block data setup<fold text='...'>
    integer A,B,C
</fold>end