program a<fold text='...'>
    b1: block<fold text='...'>
        integer a

        cr: critical<fold text='...'>
            a = 4
        </fold>endcritical cr
    </fold>endblock

    b2: block<fold text='...'>
        as: associate e=>1+4, b=>2*3<fold text='...'>
            c = 3
        </fold>end associate
    </fold>end block

    forall (i = 1 : 10 : 2)<fold text='...'>
        forall (j = 2 : 20 : 4, i = 1 : 10 : 2, j /= i) a = 2
        1 forall (i = 1 : 10 : 2)<fold text='...'>
        2    v = 1
        3 </fold>endforall
        where (l < 0) b = 1
        where (k < 0)<fold text='...'>
            c = 4</fold>
        elsewhere (k < 3)<fold text='...'>
            c = 5</fold>
        elsewhere<fold text='...'>
            c = 6</fold>
        endwhere
    </fold>endforall

    select case (a+b)<fold text='...'>
    case (1,2)<fold text='...'>
        c=1</fold>
    case (7:)
    case default<fold text='...'>
        c=4</fold>
    </fold>endselect

    if (.not.c) then<fold text='...'>
        d = 3.1</fold>
    else<fold text='...'>
        k = 1
        a = 2</fold>
    endif

    do i=1,5,7<fold text='...'>
      j = j-1
    </fold>enddo

    loop: do while (j < 100)<fold text='...'>
      j = j + 10
    </fold>end do loop

    do 10 i = 1, m<fold text='...'>
       a( i, j ) = zero
10  </fold>continue
</fold>endprogram