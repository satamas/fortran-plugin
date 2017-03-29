1 program a
2  do i=1,5
3    j = j+1
4  enddo

  do i=1,5,7
    j = j-1
  enddo

  loop: do while (j < 100)
    j = j + 10
  end do loop

  continue

  do concurrent (integer :: a = 1:10:2 a > 5)
    j = j-1
    j = j + 1
  enddo

5 end program a