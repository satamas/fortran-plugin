package org.jetbrains.fortran.formatter.settings

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.jetbrains.fortran.FortranLanguage

class FortranLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = FortranLanguage.INSTANCE

override fun getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType): String
            = """#include 'include.f95'
!comment
data binary / B'101'/
data octal  / O'765'/
data hex    / Z'4'  /
real W(100,100)[0:2,*]
integer, dimension(2) :: array = (/ 1, 2 /)
logical b = .true.
format (1PE12.4, I10)
if (a .le. 10 .and. b > 12 .or. c)
   c = (12**2 + 12.3d0 < v%res) .eqv. .not. (a .definedoperator. b)
else
   write(*,*) "Test output"
endif
end"""

     override fun getIndentOptionsEditor(): IndentOptionsEditor? = SmartIndentOptionsEditor()

     override fun getDefaultCommonSettings(): CommonCodeStyleSettings? {
        val defaultSettings = CommonCodeStyleSettings(FortranLanguage.INSTANCE)
        val indentOptions = defaultSettings.initIndentOptions()
        indentOptions.INDENT_SIZE = 4
        indentOptions.CONTINUATION_INDENT_SIZE = 8
        indentOptions.TAB_SIZE = 4
        indentOptions.USE_TAB_CHARACTER = false

        defaultSettings.BLOCK_COMMENT_AT_FIRST_COLUMN = false
        defaultSettings.LINE_COMMENT_AT_FIRST_COLUMN = true
        return defaultSettings
    }
}