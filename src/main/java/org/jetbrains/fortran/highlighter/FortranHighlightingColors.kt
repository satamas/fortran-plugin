package org.jetbrains.fortran.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as DefColors
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.options.colors.AttributesDescriptor

//import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

enum class FortranHighlightingColors (humanName: String, val default: TextAttributesKey ) {
    LINE_COMMENT("FORTRAN_COMMENT", DefColors.LINE_COMMENT),
    KEYWORD("FORTRAN_KEYWORD", DefColors.KEYWORD),
    NUMBER("FORTRAN_NUMBER", DefColors.NUMBER),
    STRING("FORTRAN_STRING", DefColors.STRING),
    BAD_CHARACTER("FORTRAN_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("org.jetbrains.fortran.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}

//    public static final TextAttributesKey DIRECTIVE = createTextAttributesKey("FORM_DIRECTIVE", DefaultLanguageHighlighterColors.METADATA);
//    public static final TextAttributesKey PARENTHESIS = createTextAttributesKey("FORM_PARENTHESIS", DefaultLanguageHighlighterColors.PARENTHESES);
//    public static final TextAttributesKey BRACKETS = createTextAttributesKey("FORM_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);