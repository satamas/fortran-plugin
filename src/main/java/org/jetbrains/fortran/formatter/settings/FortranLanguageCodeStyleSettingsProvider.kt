package org.jetbrains.fortran.formatter.settings

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.jetbrains.fortran.FortranLanguage

class FortranLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = FortranLanguage.INSTANCE

override fun getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType): String
            = "program sample\n" +
              "    a = +1\n" +
              "    b = (1 + 2) * a + 2**2\n" +
              "    f = .not. (2 .definedoperator. 1)\n" +
              "    if (b .eq. 10 .eqv. (c > 10) .and. f) then\n" +
              "        write (*,*) 'string1' // 'string2', b\n" +
              "    endif\n" +
              "end program sample\n"

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                        "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                        "SPACE_AROUND_LOGICAL_OPERATORS",
                        "SPACE_AROUND_EQUALITY_OPERATORS",
                        "SPACE_AROUND_RELATIONAL_OPERATORS",
                        "SPACE_AROUND_ADDITIVE_OPERATORS",
                        "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
                        "SPACE_AROUND_UNARY_OPERATOR",
                        "SPACE_AFTER_COMMA",
                        "SPACE_BEFORE_COMMA"
                )
                consumer.renameStandardOption("SPACE_AROUND_ASSIGNMENT_OPERATORS", "Space around assigment operators (=, =>)")
                consumer.renameStandardOption("SPACE_AROUND_LOGICAL_OPERATORS", "Logical operators (.or., .and.)")
                consumer.renameStandardOption("SPACE_AROUND_EQUALITY_OPERATORS", "Equality operators (==, .eq., ...)")
                consumer.renameStandardOption("SPACE_AROUND_RELATIONAL_OPERATORS", "Relational operators (<, .lt., ...)")
                consumer.renameStandardOption("SPACE_AROUND_MULTIPLICATIVE_OPERATORS", "Multiplicative operators (*, /)")
                consumer.renameStandardOption("SPACE_AROUND_UNARY_OPERATOR", "Unary operators (+, -)")

                consumer.showCustomOption(FortranCodeStyleSettings::class.java, "SPACE_AROUND_NOT_OPERATOR", "Not operator (.not.)",
                        CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS)
                consumer.showCustomOption(FortranCodeStyleSettings::class.java, "SPACE_AROUND_POWER_OPERATOR", "Power operator (**)",
                        CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS)
                consumer.showCustomOption(FortranCodeStyleSettings::class.java, "SPACE_AROUND_EQUIVALENCE_OPERATOR", "Equivalence operators (.eqv., .neqv.)",
                        CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS)
                consumer.showCustomOption(FortranCodeStyleSettings::class.java, "SPACE_AROUND_CONCAT_OPERATOR", "Concatenation operator (//)",
                        CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS)
                consumer.showCustomOption(FortranCodeStyleSettings::class.java, "SPACE_AROUND_DEFINED_OPERATOR", "Defined operators",
                        CodeStyleSettingsCustomizable.SPACES_AROUND_OPERATORS)
            }

        }
    }

     override fun getIndentOptionsEditor(): IndentOptionsEditor = SmartIndentOptionsEditor()

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