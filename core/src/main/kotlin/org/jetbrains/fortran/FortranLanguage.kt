package org.jetbrains.fortran

import com.intellij.lang.Language

object FortranLanguage : Language("Fortran", "text/fortran") {
    private fun readResolve(): Any = FortranLanguage
    override fun isCaseSensitive() = false

    override fun getDisplayName() = "Fortran"
}
