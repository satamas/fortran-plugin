module operators
    interface operator(+)
        procedure :: foo_plus, fooo_plus
    end interface
    interface operator(*)
        procedure :: bar
    end interface
    interface assignment(=)
        procedure :: foo_bar
    end interface
end module operators