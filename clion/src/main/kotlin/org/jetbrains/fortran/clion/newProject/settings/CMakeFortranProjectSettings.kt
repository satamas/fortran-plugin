package org.jetbrains.fortran.clion.newProject.settings

import com.jetbrains.cidr.cpp.cmake.projectWizard.generators.settings.CMakeProjectSettings

class CMakeFortranProjectSettings : CMakeProjectSettings() {
    override fun getEnabledProjectLanguages() = "Fortran"

    override fun getLanguageVersionLineForCMake() = ""
}