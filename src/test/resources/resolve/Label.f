      program labeltests

      do 10 i = 1, 3
          a = 3
4         do 100 i = 4, 5
              aa = 10
              bb = 2
100           cc = 3
          b = 4
          do 0010 j = 1, 4
              a = 1
              do <caret>010 k = 3, 5
                  b = k
00010            enddo
      d = 8
      do 20 k = 3, 5
          b = 1
20    continue

      end program