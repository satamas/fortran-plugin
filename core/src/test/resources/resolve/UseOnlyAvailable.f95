   use TestModule, only : dot
   type(dot) :: dot1
   write(*,*) dot1%<caret>x
end