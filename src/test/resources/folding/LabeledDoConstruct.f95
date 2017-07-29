program labeltests<fold text='...'>
    do 10 i=1,3<fold text='...'>
        1      a = 3
        3      do 100 i=4,5<fold text='...'>
            aa = 1
            bb = 2
    100 </fold>end do
        2      b = 4
    </fold>do 10 j=1,4<fold text='...'>
            a = 1
    </fold>do 10 k=3,5<fold text='...'>
                b = 1
    10 </fold>enddo
    11   d = 8
</fold>end program