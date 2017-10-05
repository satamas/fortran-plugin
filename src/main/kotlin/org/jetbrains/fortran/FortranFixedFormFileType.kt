package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.LanguageFileType

object FortranFixedFormFileType : LanguageFileType(FortranFixedFormLanguage) {
    override fun getName() = "Fortran fixed form"

    override fun getDescription() = "Fortran language file with fixed source form"

    override fun getDefaultExtension() = "f"

    override fun getIcon() = FortranIcons.fileTypeIcon
}
