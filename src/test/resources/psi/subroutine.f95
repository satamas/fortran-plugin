 subroutine message_number_dp(level,text,number,format,units)
    implicit none
    integer,intent(in)          :: level
    character(len=*),intent(in) :: text
    real(dp),intent(in)         :: number
    character(len=*),intent(in) :: format,units
    character(len=20)           :: char_number
    write(char_number,format) number
    if(level <= verbose_level) write(*,*) trim(text)//' '//trim(adjustl(char_number))//' '//trim(units)
  end subroutine message_number_dp