package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.LanguageFileType

object FortranFixedFormFileType : LanguageFileType(FortranFixedFormLanguage) {
    val DEFAULT_EXTENSIONS = arrayOf("f", "for", "F", "FOR")

    override fun getName() = "Fortran fixed form"

    override fun getDescription() = "Fortran language file with fixed source form"

    override fun getDefaultExtension() = DEFAULT_EXTENSIONS[0]

    override fun getIcon() = FortranIcons.fileTypeIcon
}
