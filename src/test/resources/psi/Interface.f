      module gaussinter
      interface solve
        function solve(n, pmatrix, key);
             ! NOTE: real *8 is not an official standard but works usually
              real(8), dimension (:), pointer ::  solve;
              real(8), dimension (:,:), pointer :: pmatrix;
              integer n, key
        end function solve
      end interface
      end module