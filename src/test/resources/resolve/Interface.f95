function NewtonN(f, x_old)

    use GaussSolveSystem

    implicit none
    external :: f

    interface
        function f(x)
            real(8), dimension (:), pointer :: f, x
        end function f
    end interface

    real(8), dimension (:, :), pointer :: pmatrix, temp_mat
    real(8), dimension (:), pointer :: x, x_old, temp, NewtonN, x_temp
    integer :: cnt, i, j, NN, smth
    real(8) :: eps, dx

    ! Инициализация метода
    allocate (x(1 : N))

    call read_options(NN, eps, dx)

    allocate (temp_mat(0 : N, 1 : N))

    ! Решение
    newt_c : do cnt = 1, NN
        ! Вычисление значений функций в точках
        temp => f(x_old)
        temp_mat(0, 1 : n) = temp(1 : n)
        deallocate (temp)

        allocate (x_temp(1 : n))
        do i = 1, N
            x_temp = x_old
            x_temp(i) = x_temp(i) + dx
            temp => f(x_temp)
            temp_mat(i, 1 : n) = temp
            deallocate (temp)
        enddo
        deallocate (x_temp)


        ! ОСЛУ
        allocate (pmatrix(1 : n + 1, 1 : n))

        line : do i = 1, N
            pmatrix(n + 1, i) = -temp_mat(0, i)

            col : do j = 1, N
                pmatrix(j, i) = (temp_mat(j, i) - temp_mat(0, i)) / dx
                pmatrix(n + 1, i) = pmatrix(n + 1, i) + pmatrix(j, i) * x_old(j)
            enddo col
        enddo line

        x => GaussSS(N, pmatrix, 3)

        call swap_v(x, x_old)

        ! Выход
        if (abs(sum((x - x_old)**2)) < eps) then
            write (*, *) "Solved."
            exit
        endif
    enddo newt_c

    temp => <caret>f(x)
    write(*, *) 'Mistake', sum(temp**2)

    !Завершение работы
    deallocate (temp)
    deallocate (temp_mat)
    deallocate (x)
    NewtonN => x_old

end function NewtonN