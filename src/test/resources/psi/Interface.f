      module gaussinter
      interface solve
        function solve(n, pmatrix, key);
             ! NOTE: real *8 is not an official standard but works usually
              real*8, dimension (:), pointer ::  solve;
              real(8), dimension (:,:), pointer :: pmatrix;
              integer n, key
        end function solve
        subroutine LPV(n, x, P, dP)
          real(8) :: x
          real(8), pointer :: P, dP
          integer :: n
        end subroutine LPV
      end interface
      end module