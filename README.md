# Fortran IntelliJ Idea plugin

Status
======

The work on the plugin is in progress now. Implemented features are mentioned in the section General Usage Information. 
There might be some problems and mistakes in the plugin. If you see one, please, do not hesitate to report us.

Installation
============

The plugin is compatible with any IntelliJ based IDE starting from 2017.1. 
If you don't have any yet, try [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) for free. 
In the future some opportunities will be available only in CLion.
  
You can install a stable version of the plugin from 
[Jet Brains plugins repository](https://plugins.jetbrains.com/plugin/9699-fortran) 
using `Settings > Plugins > Browse repositories` in your IntelliJ based IDE.


Fortran plugin is [gradle](https://gradle.org/) based project. So, it can be build using `gradlew assemble` command, 
tested with `gradlew test` command and launched with `gradlew runIdea` command 

General Usage Information
=========================

To start work with your Fortran project use `New > Project from existing sources` action. 
In CLion it is also possible to import project using `CMake`. It allows to compile and run project.

Fortran source files might be in two forms: free form and fixed form. 
Fixed form is deprecated, so some features are available only for free form now.
The plugin differs source forms by the file extension. 
`.f` and `.for` files are handled as fixed form source files 
while `.f90`, `.f95`, `.f03` and `.f08` files are in free source form.
One can easily change this settings. Please, visit 
[File Types Help](https://www.jetbrains.com/help/idea/file-types.html) for further detail.

Now the plugin provides lexer, parser, simple syntax highlighter, formatter (for free source firm files only), 
commenter (for free source firm files only) and source folder. Navigation (Find Usages and Go to Definition) 
will be added soon. 

Syntax highlighter has color settings available in 
`File > Settings > Editor > Color Sheme > Fortran`. Detailed information about colors and fonts is available 
[here](https://www.jetbrains.com/help/idea/configuring-colors-and-fonts.html)

[Code Style Settings](https://www.jetbrains.com/help/idea/configuring-code-style.html) 
are in `File > Settings > Editor > Code Style > Fortran`. To Reformat code use
`Code > Reformat Code` action or `Ctrl+Alt+l`.

To fold the code use code folding toggles ![Toggle Minus Start](https://www.jetbrains.com/help/img/idea/2017.2/foldingMinusStart.png),
![Toggle Minus End](https://www.jetbrains.com/help/img/idea/2017.2/foldingMinusEnd.png) and
![Toggle Plus](https://www.jetbrains.com/help/img/idea/2017.2/foldingPlus.gif).

To comment or uncomment code lines press `Ctrl + /`.

