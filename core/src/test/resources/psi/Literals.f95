program literals
  ENUM, BIND(C)
    ENUMERATOR :: RED = 4, BLUE = 9
    ENUMERATOR YELLOW
  END ENUM

  a = 1
  b = 1_8
  c = 1.1
  d = 1.1_4
  e = 1e4_4+2e+4_8
  f = 1_"Deidranna &
  &rules!"
  str = 'Very ''&\\ bad & &
  & &
  & string\'
  g = B"101"
  h = O"1765"
  i = Z'9A7f'
  j = .TRUE._1
  k = ( 10.0, +4_8)
  l = (/ 10, 9, 8, 7 /)
  m = [ 1, 2, 3 ]
endprogram