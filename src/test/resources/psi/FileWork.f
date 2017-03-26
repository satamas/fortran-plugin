program filework
    open (1, file="data.dat")
    open (unit = 2, file="res.dat", err=12345)
    wait (1, end=1, eor = 2, err =3, id = 2+2, iomsg = aaa, iostat = bbb)
    backspace 1
    backspace (1, err = 1)
    endfile 1
    endfile (2, iomsg = ccc)
    rewind 1
    flush 2
    flush (unit=2, err = 3)
    close (1, iostat = i, err = 12345)
    close (unit = 2)
end