program a
  IMPLICIT DOUBLE PRECISION (A-H,O-Z)
  character*80, allocatable :: lines_speciesname(:)
  integer a
  character c * 8
  character c * (*)
  integer, external :: b, c
  integer d (*)
  integer d (3+2)
  integer d (3+2:*)
  integer d (4,3+2:*)
  parameter (flag1 = .true.)
  parameter (str='msg')
  parameter (epsilon=1.0e-6, pi=3.141593)
  common c
  common c, d(1)
  common // c
  common /x/ c
  common /x/ c, /y/ d(1)
  common /x/ c /y/ d(1)
  double complex :: var1
  doublecomplex :: var2
end program a