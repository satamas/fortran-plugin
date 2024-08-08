package org.jetbrains.fortran

import com.intellij.lang.Language

object FortranFixedFormLanguage : Language(FortranLanguage, "Fortran fixed form", "text/fortran") {
    private fun readResolve(): Any = FortranFixedFormLanguage
    override fun isCaseSensitive() = false

    override fun getDisplayName() = "Fortran (fixed form)"
}
