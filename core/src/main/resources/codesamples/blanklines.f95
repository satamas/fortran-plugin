module a

    real :: q


contains


    module procedure aa

        x=y+z


    contains


        real function b(z)

            return z*2


        endfunction


        real function c(z)

            return z*3


        endfunction


    endprocedure


endmodule


submodule (a) suba


contains


    subroutine s()

    endsubroutine


endsubmodule
