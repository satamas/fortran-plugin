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
        where (k < 0)
            c = 4
        elsewhere (k < 3)
            c = 5
        elsewhere
            c = 6
        endwhere
    </fold>endforall
</fold>endprogram