package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfigurationSettingsEditor
import com.jetbrains.cidr.cpp.execution.CMakeRunConfigurationType
import org.jetbrains.fortran.FortranIcons

class FortranRunConfigurationType
    : CMakeRunConfigurationType("FortranRunConfiguration",
        "Application",
        "Fortran",
        "Fortran CMake run configuration",
        FortranIcons.runConfigurationIcon) {

    override fun createRunConfiguration(project: Project, factory: ConfigurationFactory): FortranRunConfiguration {
        return FortranRunConfiguration(project, factory, "")
    }

    override fun createEditor(project: Project): SettingsEditor<out CMakeAppRunConfiguration> {
        return CMakeAppRunConfigurationSettingsEditor(project, getHelper(project))
    }
}
