subroutine printCommon
    common /block1/ x, y /block2/ z
    write (*,*) x, y
end subroutine

subroutine sumCommon
    common /block2/ z, /block3/ i, j, k
    write (*,*) z, i+j+k
end subroutine