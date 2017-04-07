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
  f = 1_"Deidranna rules!"
  g = B"101"
  h = O"1765"
  i = Z'9A7f'
  j = .TRUE._1
endprogram