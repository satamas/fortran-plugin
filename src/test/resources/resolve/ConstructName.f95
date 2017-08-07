program labeltests

    do 10 i = 1, 3
        a = 3
        4 do 100 i = 4, 5
            aa = 10
            bb = 2
        100 cc = 3
        b = 4
        do 10 j = 1, 4
            a = 1
            do 10 k = 3, 5
            b = k
            10 enddo
            d = 8
            do 20 k = 3, 5
                b = 1
            20 continue

    ifconstruct : if (1) then
        write(*, *) "1"
    elseif (2) then <caret>ifconstruct
        write(*, *) "2"
    else
        write(*, *) "3"
    endif ifconstruct

    loop: do i=1,3
        a=2
    enddo loop
end program