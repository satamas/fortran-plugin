package org.jetbrains.fortran.ide.newProject

enum class FortranStandards(val standard: String) {
    F90("90");

    val displayString = "F" + standard

    fun fromDisplayString(displayString: String): String {
        return displayString.removePrefix("f")
    }
}