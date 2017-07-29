package org.jetbrains.fortran

import com.intellij.lang.Language

object FortranFixedFormLanguage : Language("Fortran fixed form", "text/fortran") {
    override fun isCaseSensitive() = false

    override fun getDisplayName() = "Fortran (fixed form)"
}
