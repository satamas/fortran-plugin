package org.jetbrains.fortran.ide.highlighter

import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.jetbrains.fortran.ide.formatter.settings.FortranCodeStyleSettingsProvider

class FortranColorSettingsPage : ColorSettingsPage {
    private val ATTRIBUTES = FortranHighlightingColors.values().map { it.attributesDescriptor }.toTypedArray()

    private val ANNOTATOR_TAGS = FortranHighlightingColors.values().associateBy({ it.name }, { it.textAttributesKey })

    override fun getDisplayName() = "Fortran"
    override fun getIcon() = null
    override fun getAttributeDescriptors() = ATTRIBUTES
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter() = FortranHighLighter(false)
    override fun getAdditionalHighlightingTagToDescriptorMap() = ANNOTATOR_TAGS
    override fun getDemoText(): String = FortranCodeStyleSettingsProvider.getCodeSample("Colors.f95")
}

