real function average()
    integer c, z
    intrinsic sin, cos
    external sin, cos
    common /x/ c
    save
    save z
    save z, /x/
    entry average2(x, y)
    EQUIVALENCE (A,B)
    EQUIVALENCE (A(1), B)
    EQUIVALENCE (A(1), B(1)(2+1:))
    EQUIVALENCE (A(1), B(1)(2+1:3+2))
    EQUIVALENCE (A(1+2, 3),B), (A(2), B(1)(2+1:3+2))
    return
end