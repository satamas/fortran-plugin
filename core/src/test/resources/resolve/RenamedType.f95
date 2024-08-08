    use TestModule, only : local_cut => cut
    type(local_cut) :: c, cut
    write(*, *) c%a%<caret>x
    end