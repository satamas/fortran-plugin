package org.jetbrains.fortran.highlighter

import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class FortranColorSettingsPage : ColorSettingsPage {
    private val ATTRIBUTES = FortranHighlightingColors.values().map { it.attributesDescriptor }.toTypedArray()

    private val ANNOTATOR_TAGS = FortranHighlightingColors.values().associateBy({ it.name }, { it.textAttributesKey })

    override fun getDisplayName() = "Fortran"
    override fun getIcon() = null
    override fun getAttributeDescriptors() = ATTRIBUTES
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter() = FortranHighLighter(false)
    override fun getAdditionalHighlightingTagToDescriptorMap() = ANNOTATOR_TAGS
    override fun getDemoText() : String = """!comment
data binary / B'101'/
data octal  / O'765'/
data hex    / Z'4'  /
real W(100,100)[0:2,*]
integer, dimension(2) :: array = (/ 1, 2 /)
logical b = .true.
if (a .le. 10 .and. b > 12 .or. c)
   c = (12**2 + 12.3d0 < v%res) .eqv. .not. (a .definedoperator. b)
else
   write(*,*) "Test output"
endif
end"""

}

