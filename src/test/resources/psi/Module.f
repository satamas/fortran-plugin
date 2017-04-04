1 module a
2   real :: q
3 contains
4   module procedure aa
5     x=y+z
6     contains
7     real function b(z)
8       return z*2
9     endfunction
10   endprocedure
11   endmodule
12 submodule (a) suba
13 contains
14 subroutine s()
15 endsubroutine
16 endsubmodule
