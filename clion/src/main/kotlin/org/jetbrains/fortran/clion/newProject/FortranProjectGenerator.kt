package org.jetbrains.fortran.clion.newProject

import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.cidr.cpp.cmake.projectWizard.CLionProjectWizardUtils.getCMakeFileHeader
import com.jetbrains.cidr.cpp.cmake.projectWizard.generators.CMakeProjectGenerator
import org.jetbrains.fortran.clion.newProject.settings.CMakeFortranProjectSettings

class FortranProjectGenerator : CMakeProjectGenerator() {
    init {
        myLanguageVersion = FortranStandards.F90.displayString
    }

    override fun getName() = "Fortran executable"

    override fun createSourceFiles(name: String, dir: VirtualFile) = arrayOf(
            createProjectFileWithContent(dir, "main.f90",
                    "program $name\n" +
                            "    write(*,*) \"Hello, World!\"\n" +
                            "end program\n"
            )
    )


    override fun getCMakeFileContent(projectName: String): String {
        return getCMakeFileHeader(projectName, cMakeProjectSettings) +
                "\n" +
                "enable_language (Fortran)\n" +
                "add_executable($projectName main.f90)"
    }

    override fun getLanguageVersions(): Array<String> = FortranStandards.values().map { it.displayString }.toTypedArray()

    override fun createProjectSettings() = CMakeFortranProjectSettings()
}