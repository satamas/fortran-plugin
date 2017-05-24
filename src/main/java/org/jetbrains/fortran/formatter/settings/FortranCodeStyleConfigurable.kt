package org.jetbrains.fortran.formatter.settings

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.fortran.FortranLanguage

class FortranCodeStyleConfigurable(settings: CodeStyleSettings, cloneSettings: CodeStyleSettings)
    : CodeStyleAbstractConfigurable(settings, cloneSettings, FortranLanguage.displayName) {
    override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
        return FortranCodeStyleMainPanel(currentSettings, settings)
    }

    override fun getHelpTopic(): String? = null

    private class FortranCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings)
        : TabbedLanguageCodeStylePanel(FortranLanguage, currentSettings, settings) {

        override fun addWrappingAndBracesTab(settings: CodeStyleSettings) {}
    }
}