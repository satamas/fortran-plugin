package org.jetbrains.fortran.ide.highlighter

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
    override fun getDemoText() : String = """#include 'include.f95'
!comment
<KEYWORD>data</KEYWORD> <IDENTIFIER>binary</IDENTIFIER> / B'101'/
<KEYWORD>data</KEYWORD> <IDENTIFIER>octal</IDENTIFIER>  / O'765'/
<KEYWORD>data</KEYWORD> <IDENTIFIER>hex</IDENTIFIER>    / Z'4'  /
<KEYWORD>real</KEYWORD> <IDENTIFIER>W</IDENTIFIER>(100,100)[0:2,*]
<KEYWORD>integer</KEYWORD>, <KEYWORD>dimension</KEYWORD>(2) :: <IDENTIFIER>array</IDENTIFIER> = (/ 1, 2 /)
<KEYWORD>logical</KEYWORD> <IDENTIFIER>b</IDENTIFIER> = .true.
<KEYWORD>format</KEYWORD> (1PE12.4, I10)
<KEYWORD>if</KEYWORD> (<IDENTIFIER>a</IDENTIFIER> .le. 10 .and. <IDENTIFIER>b</IDENTIFIER> > 12 .or. <IDENTIFIER>c</IDENTIFIER>)
   <IDENTIFIER>c</IDENTIFIER> = (12**2 + 12.3d0 < <IDENTIFIER>v</IDENTIFIER>%<IDENTIFIER>res</IDENTIFIER>) .eqv. .not. (<IDENTIFIER>a</IDENTIFIER> .definedoperator. <IDENTIFIER>b</IDENTIFIER>)
<KEYWORD>else</KEYWORD>
   <KEYWORD>write</KEYWORD>(*,*) "Test output"
<KEYWORD>endif</KEYWORD>
<KEYWORD>end</KEYWORD>"""

}

