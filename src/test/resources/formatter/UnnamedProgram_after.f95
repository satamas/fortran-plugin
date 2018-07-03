real (8), allocatable, dimension(:), target :: x
real (8), dimension(:), pointer :: px
integer, dimension (1:1000), intent (in) :: y
integer, dimension (9, 0:99, -99:99) :: i
endprogram arraystest