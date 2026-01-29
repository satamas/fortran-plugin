package org.jetbrains.fortran.ide.formatter.settings

import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import org.jetbrains.fortran.FortranLanguage

class FortranCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun getLanguage() = FortranLanguage

    override fun createCustomSettings(settings: CodeStyleSettings) = FortranCodeStyleSettings(settings)

    override fun getConfigurableDisplayName(): String = "Fortran"

    override fun createConfigurable(
        settings: CodeStyleSettings,
        originalSettings: CodeStyleSettings
    ): CodeStyleConfigurable = FortranCodeStyleConfigurable(settings, originalSettings)
}


