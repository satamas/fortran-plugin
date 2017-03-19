program a
  do i=1,5
    j = j+1
  enddo

  do i=1,5,7
    j = j-1
  continue

  loop: do while (j < 100)
    j = j + 10
  end do loop

  do concurrent (integer :: a = 1:10:2 a > 5)
    j = j-1
    j = j + 1
  continue

end program a