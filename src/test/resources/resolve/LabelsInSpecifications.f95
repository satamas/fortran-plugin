program labelsInSpecifications
    open(1, file='file.txt')
    write(1,<caret>1,err=2) 3.1415927, 3.1415927
    1  format (6x, e15.7, 3x, e6.3)
    2  write(*, *) "oops"
    close(1)
end
