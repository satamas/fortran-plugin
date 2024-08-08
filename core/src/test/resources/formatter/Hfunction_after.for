      program Hfunct
      implicit none

      interface
        subroutine getmem(n, er, fv, sv, dots, scal)
          real (8),allocatable,dimension(:),target ::
     *     dots,scal,fv,sv
          integer n
          byte er
        end subroutine getmem

        subroutine freemem(fv, sv, dots, scal)
           real (8),allocatable,dimension(:),target :: dots,scal,fv,sv
        end subroutine freemem

        subroutine work(n, A, fv, sv, dots, scal)
          real (8),allocatable,dimension(:),
     *    target :: dots,scal,fv,sv
          integer n
          real *8 A
        end subroutine work

        subroutine gauss(ler, n, dots, scal)
          real (8),allocatable,dimension(:),target :: dots,scal
          integer n
          byte ler
        end subroutine gauss
      end interface

      real (8),allocatable,dimension(:),target :: dots,scal,fv,sv
      real *8 A
      integer n, j
      byte er

      write (*,*) 'AAA'
      read (*,*) n
      if (n .lt. 1) then
        write (*,*)'Problems'
        GOTO 100
      endif

      call getmem(n, er, fv, sv,dots, scal)
      if (er .ne. 0) then
        write (*,*) 'AAA'
      else

        write (*,*) 'AAA'

      do j=1, n
        write (*,*) dots(j), scal(j)
      enddo


        call work (n, A, fv, sv, dots, scal)

      endif

      call freemem(fv, sv, dots, scal)
      write (*,*) 'AAA'

 100  continue
      end

c AAA
c AAA
      subroutine getmem(n, er, fv, sv, dots, scal)
      implicit none
      real (8),allocatable,dimension(:),target :: dots,scal,fv,sv
      integer n
      byte er, ler

      er=1
      allocate (dots(0:n), stat=ler)
      if (ler .eq. 0) then
        allocate (scal(1:n), stat=ler)
        if (ler .eq. 0) then
          call gauss(ler, n, dots, scal)
          if (ler .eq. 0) then
            allocate (fv(0:n), stat=ler)
            if (ler .eq. 0) then
              allocate (sv(0:n), stat=ler)
              if (ler .eq. 0) then
                er=0
              endif
            endif
          endif
        endif
      endif
      end


c AAA
      subroutine freemem(fv, sv, dots, scal)
      implicit none
      real (8),allocatable,dimension(:),target :: dots,scal,fv,sv
      byte er

      if ( allocated(sv) )    deallocate (sv, stat=er)
      if ( allocated(fv) )    deallocate (fv, stat=er)
      if ( allocated(scal) )  deallocate (scal, stat=er)
      if ( allocated(dots) )  deallocate (dots, stat=er)
      end


c AAA
      subroutine work (n, A, fv, sv, dots, scal)
      implicit none
      real (8),allocatable,dimension(:),target :: dots,scal,fv,sv
      real (8) A
      integer n


c Don't work!!!
      end

c AAA


