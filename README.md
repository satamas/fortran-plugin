# Fortran IntelliJ plugin

<!-- Plugin description -->
Fortran language support

The plugin supports FORTRAN 77, FORTRAN 90, FORTRAN 95, FORTRAN 2003 and FORTRAN 2008 source code.
Several nonstandard extensions are also supported.
Lexer, parser, syntax highlighter, debugger, commenter, formatter, folding, navigation, structure view, brace matcher and some inspections are provided.
<!-- Plugin description end -->

Status
======

The work on the plugin is in progress now. Implemented features are mentioned in the section General Usage Information. 
There might be some problems and mistakes in the plugin. If you see one, please, do not hesitate to report us.

Installation
============

The plugin is compatible with all IntelliJ based IDEs, but it works better with CLion if you are using CMake as a build tool. 
If you don't have any IntelliJ ide yet, you can try [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) for free.
  
You can install a stable version of the plugin from 
[Jet Brains plugins repository](https://plugins.jetbrains.com/plugin/9699-fortran) 
using `Settings > Plugins > Browse repositories` in your IDE.


Fortran plugin is [gradle](https://gradle.org/) based project. So, it can be build using `gradlew assemble` command, 
tested with `gradlew test` command and launched with `gradlew runIdea` command 

General Usage Information
=========================

To start work with your Fortran project use `New > Project from existing sources` action. 
In CLion it is also possible to load `CMake` based projects, see 
[instruction](https://www.jetbrains.com/help/clion/quick-cmake-tutorial.html). 
Example of Fortran project build with `CMake` can be found [here](https://gitlab.kitware.com/cmake/community/wikis/doc/cmake/languages/fortran/ForFortranExample). 
Using `CMake` project in CLion allows you to compile, run and debug project from the IDE.

Since CLion 2018.1 frotran project can be debugged with default `Application` run configuration. 
To debug Fortran projects in previous versions of CLion please add Fortran run configuration `Edit Configurations > Add New Configuration > Fortran`, 
select it in `Select Run/Debug configuration` menu and choose GDB as a working debugger. More detail about debugging in CLion
is available in [CLion help](https://www.jetbrains.com/help/clion/debugger.html).

Fortran source files might be in two forms: free form and fixed form. 
For now some features are available only for free form.
The plugin differs source forms by the file extension. 
`.f` and `.for` files are handled as fixed form source files 
while `.f90`, `.f95`, `.f03` and `.f08` files are in free source form.
One can easily change this settings. Please, visit 
[File Types Help](https://www.jetbrains.com/help/idea/file-types.html) for further detail.

Now the plugin provides lexer, parser, debugger, simple syntax highlighter, formatter (for free source firm files only), 
commenter, code folding and navigation (Find Usages and Go to Definition), brace matcher, structure view and several code inspections.

