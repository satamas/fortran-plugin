program p
    if(1-2) 10, 20, 30
    if ( value .le. atad ) call punt
    assign 10 to a
    backspace 2
    backspace a
    backspace ( 2, iostat=code, err=9 )
    call oops ( text )
    close ( 2, status='delete', iostat=i )
    continue
    endfile ( unit=nout, iostat=kode, err=9)
    goto 10
    goto n, (10)
    go to n ( 10, 20, 30, 40 )
    go to ( 10, 20, 30, 40 ), n + 2
end program