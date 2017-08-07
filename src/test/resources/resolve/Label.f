      program labeltests

      do 10 i = 1, 3
          a = 3
4         do 100 i = 4, 5
              aa = 10
              bb = 2
100           cc = 3
          b = 4
          do 10 j = 1, 4
              a = 1
              do <caret>10 k = 3, 5
                  b = k
10            enddo
      d = 8
      do 20 k = 3, 5
          b = 1
20    continue

      end program