program a
  Print *, "Hello World!"
  Print *, item1
  Print *, a/100
  Print *, item1, item2, item3
!  130 format (A,E8.1)
  Print *, item1 * 5, item2 + 3, "Hello World!
  print '(12 i2)', i, j, vector
!  print *, i, j, (vector(i), i = 1, 5)
  read( 1, 2, err=8, end=9, iostat=n ) x, y
  read(*,*) a, v
  read*, a, v
  read( 3, '(5f4.1)') v
  read(unit=1, nml=g)
  write( 1, 2, err=8, iostat=n ) x, y
  write( *, * ) a, v
  write( unit=1, nml=g )
end program a