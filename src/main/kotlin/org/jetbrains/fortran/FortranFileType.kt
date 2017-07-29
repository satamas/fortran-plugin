package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.LanguageFileType

object FortranFileType : LanguageFileType(FortranLanguage) {
    val DEFAULT_EXTENSIONS = arrayOf("f90", "f95", "f03", "f08") // f15 is not ready

    override fun getName() = "Fortran"

    override fun getDescription() = "Fortran language file"

    override fun getDefaultExtension() = DEFAULT_EXTENSIONS[1]

    override fun getIcon() = FortranIcons.fileTypeIcon
}
