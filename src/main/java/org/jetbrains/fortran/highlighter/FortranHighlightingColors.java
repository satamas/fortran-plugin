package org.jetbrains.fortran.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class FortranHighlightingColors {
    public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("FORTRAN_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("FORTRAN_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey NUMBER = createTextAttributesKey("FORTRAN_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
//    public static final TextAttributesKey DIRECTIVE = createTextAttributesKey("FORM_DIRECTIVE", DefaultLanguageHighlighterColors.METADATA);
//    public static final TextAttributesKey PARENTHESIS = createTextAttributesKey("FORM_PARENTHESIS", DefaultLanguageHighlighterColors.PARENTHESES);
//    public static final TextAttributesKey BRACKETS = createTextAttributesKey("FORM_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
    public static final TextAttributesKey STRING = createTextAttributesKey("FORTRAN_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("FORTRAN_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

}

