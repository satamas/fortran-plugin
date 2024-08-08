    use TestModule
    type(cut) :: c
    integer :: x
    write(*, *) c%a%<caret>x
    write(*, *) x
    end