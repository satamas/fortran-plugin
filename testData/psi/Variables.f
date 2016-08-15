program a
  integer a
  character c * 8
  character c * (*)
  integer b, c
  integer d (*)
  integer d (3+2)
  integer d (3+2:*)
  integer d (4,3+2:*)
  common c
  common c, d(1)
  common // c
  common /x/ c
  common /x/ c, /y/ d(1)
  common /x/ c /y/ d(1)
end program a