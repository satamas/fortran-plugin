module circle
   real, parameter :: pi = 3.1415927
   real :: radius
   subroutine input(x, y, z)
    	x = y + z
   end
end module circle

real function average(x,y,z)
    sum = x + y + z
    average = sum /3.0
    entry average2(x, y)
    return
end

integer function zero()
    zero = 0
end

subroutine input(x, y, z)
 	x = y + z
end

block data setup
end