package org.jetbrains.fortran

import com.intellij.lang.Language

object FortranLanguage : Language("Fortran", "text/fortran") {
    override fun isCaseSensitive() = false

    override fun getDisplayName() = "Fortran"
}
