package org.jetbrains.fortran.ide.formatter.settings

import com.intellij.psi.codeStyle.*

class FortranCodeStyleSettings(container : CodeStyleSettings)
    : CustomCodeStyleSettings(FortranCodeStyleSettings::class.java.simpleName, container) {

    @JvmField var SPACE_AROUND_NOT_OPERATOR = true
    @JvmField var SPACE_AROUND_POWER_OPERATOR = false
    @JvmField var SPACE_AROUND_EQUIVALENCE_OPERATOR = true
    @JvmField var SPACE_AROUND_CONCAT_OPERATOR = true
    @JvmField var SPACE_AROUND_DEFINED_OPERATOR = true

    @JvmField var SPACE_BEFORE_DOUBLE_COLON = true
    @JvmField var SPACE_AFTER_DOUBLE_COLON = true

    companion object {
        @JvmField var SHOW_SOURCE_FORM_CONVERTER_PANEL = true
    }

}