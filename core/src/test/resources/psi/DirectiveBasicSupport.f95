program a
#define XXX
#define VALUE 1

#ifdef XXX
    write(*, *) "XXX defined"
#if VALUE==1
    write(*, *) "Value is 1"
#elif VALUE==2
    write(*, *) "Value is 2"
#else
    write(*, *) "Value is not 1 nor 2"
#endif
    write(*, *) "Hello !"
#else
    write(*, *) "XXX is not defined"
#endif

#if Z /* macro_eval: false */
    write(*, *) "Value is 1"
#elif ZZZ==2  /* macro_eval: false */
    write(*, *) "Value is 1"
#else

#ifndef YYY
#define YYY
    write(*, *) "YYY was not defined at this point"
#ifndef ZZZ
    write(*, *) "ZZZ is not defined"
#else
    write(*, *) "ZZZ is defined"
#endif
#else
    write(*, *) "YYY was defined at this point"
#endif

#pragma Unknown directive content doesn't change macro context state

    write(*, *) "World !"
#endif

end program
