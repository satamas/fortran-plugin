module types
  type :: person
    character(len=20) :: forename
    integer :: age
  end type person

  type :: kids
    type(person) :: oldest_child
    type(person), allocatable, dimension(:) :: other_kids
  end type kids

  TYPE, PRIVATE :: AUXILIARY
    LOGICAL :: DIAGNOSTIC
    CHARACTER (LEN = 20) :: MESSAGE
  END TYPE AUXILIARY

  TYPE NUMERIC_SEQ
    SEQUENCE
    INTEGER :: INT_VAL
    REAL :: REAL_VAL
    LOGICAL :: LOG_VAL
  END TYPE NUMERIC_SEQ

  TYPE POINT
      REAL :: X, Y
    CONTAINS
      PROCEDURE, PASS :: LENGTH => POINT_LENGTH
  END TYPE POINT
endmodule