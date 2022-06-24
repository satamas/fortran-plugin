interface operator(+)
procedure :: foo_plus, fooo_plus
end interface
interface operator( /)
procedure :: foo_div
end interface
interface operator(/ )
procedure :: foo_div2
end interface
interface operator(/ &
! comment
)
procedure :: foo_div3
end interface
interface operator(//)
procedure :: foo_divdiv
end interface
end