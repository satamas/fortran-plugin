package org.jetbrains.fortran.formatter.settings

import com.intellij.openapi.options.Configurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider

class FortranCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createCustomSettings(settings: CodeStyleSettings) = FortranCodeStyleSettings(settings)

    override fun getConfigurableDisplayName(): String? = "Fortran"

    override fun createSettingsPage(settings: CodeStyleSettings, originalSettings: CodeStyleSettings): Configurable
            = FortranCodeStyleConfigurable(settings, originalSettings)
}


