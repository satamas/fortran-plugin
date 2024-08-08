integer :: A(:), B(:)

A = B( : )
A = B( :5)
A = B(1: )
A = B(1:5)

A = B(1:5:2)
A = B(1: :2)
A = B( :5:2)
A = B( : :2)

A = B(:)
A = B(:5)
A = B(1:)

A = B(:5:2)
A = B(1::2)
A = B(::2)

end program
