real function average()
    use core_lib, only : mp_set_compression => hdf5_set_compression
    use lib, old_name => new_name
    implicit none
    implicit character (a-z)
    implicit character * (*) (c,s)
    implicit character * (1+2) (c,s)
 20 implicit complex (u, a-b,w), character*4 (c,s)
    integer c, z
    intrinsic sin, cos
    external sin, cos
    common /x/ c
    data ttl /'Arbitrary Titles'/
    data m /9/, N /0/
    data pair(1) /9.0/, VEC /3*9.0, 0.1, 0.9/
    data ( s(i,i), i=1,4)/4*1.0/
    data (( r(i,j), j=1,3), i=1,2)/6*1.0/
    save
    save z
    save z, /x/
    entry average2(x, y)
    DIMENSION M( N, N ), A(B : C)
    EQUIVALENCE (A,B)
    EQUIVALENCE (A(1), B)
    EQUIVALENCE (A(1), B(1)(2+1:))
    EQUIVALENCE (A(1), B(1)(2+1:3+2))
    EQUIVALENCE (A(1+2, 3),B), (A(2), B(1)(2+1:3+2))
    return
end