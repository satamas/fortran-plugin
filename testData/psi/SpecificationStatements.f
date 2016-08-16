real function average()
    integer c, z
    intrinsic sin, cos
    external sin, cos
    common /x/ c
    save
    save z
    save z, /x/
    entry average2(x, y)
    return
end