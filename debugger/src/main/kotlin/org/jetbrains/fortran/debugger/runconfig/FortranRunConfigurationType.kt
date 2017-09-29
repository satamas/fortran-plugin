package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfigurationSettingsEditor
import com.jetbrains.cidr.cpp.execution.CMakeRunConfigurationType
import org.jetbrains.fortran.FortranIcons

class FortranRunConfigurationType private constructor()
    : CMakeRunConfigurationType("FortranRunConfiguration",
        "Fortran",
        "Fortran CMake run configuration",
        FortranIcons.fileTypeIcon) {

    override fun createRunConfiguration(project: Project, factory: ConfigurationFactory): FortranRunConfiguration {
        return FortranRunConfiguration(project, factory, "")
    }

    override fun createEditor(project: Project): SettingsEditor<out CMakeAppRunConfiguration> {
        return CMakeAppRunConfigurationSettingsEditor(project, getHelper(project))
    }

    companion object {

        val instance: FortranRunConfigurationType
            get() = ConfigurationTypeUtil.findConfigurationType(FortranRunConfigurationType::class.java)
    }
}
