program select_test
  select case (a+b)
    case (1,2)
       c=1
    case (:0)
       c=0
    case (3:5)
       c=2
    case (7:)
    case default
       c=4
  endselect
end program select_test