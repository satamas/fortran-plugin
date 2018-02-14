program fileUnitNumber
    open(<caret>1, file='file1.txt')
    open(2, file='file2.txt')
    write(1,1,err=2) 3.1415927, 3.1415927
    1  format (6x, e15.7, 3x, e6.3)
    2  write(*, *) "oops"
    close(1)
    close(2)
end