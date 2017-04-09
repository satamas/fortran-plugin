program p
    if ( l ) then
      k=k+1
    end if
    if ( l ) then
      n=n+1
    else
      k=k+1
    end if
    if ( pressure .gt. 1000.0 ) then
      if ( n .lt. 0.0 ) then
        x = 0.0
        y = 0.0
      else
        z = 0.0
      end if
    else if ( temperature .gt. 547.0 ) then
      z = 1.0
    else
      x = 1.0
      y = 1.0
    end if
    do 200 k = 5, 1
    if(1-2) 10, 20, 30
    if ( values .le. atad ) call punt
!    deprecated
!    assign 10 to a
    backspace 2
    backspace a
    backspace ( 2, iostat=code, err=9 )
    call oops ( text )
    close ( 2, status='delete', iostat=i )
    continue
    endfile ( unit=nout, iostat=kode, err=9)
    goto 10
!    goto n, (10)
!    go to n ( 10, 20, 30, 40 )
    go to ( 10, 20, 30, 40 ), n + 2
    inquire ( 3, opened=ok, named=hasname, name=fn )
    open(unit=8, file='projecta/data.test', err=99)
!    pause 1
!    pause "p"
    return 2
    rewind 3
    rewind (unit = 3, iostat = code, err = 100)
    sphere ( r ) = 4.0 * pi * (r**3) / 3.0
    stop 9
    stop 'error'
    enddo
end program