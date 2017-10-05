package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.LanguageFileType

object FortranFileType : LanguageFileType(FortranLanguage) {
    override fun getName() = "Fortran"

    override fun getDescription() = "Fortran language file"

    override fun getDefaultExtension() = "f90"

    override fun getIcon() = FortranIcons.fileTypeIcon
}
