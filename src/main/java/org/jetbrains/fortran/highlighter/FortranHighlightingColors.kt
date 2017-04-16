package org.jetbrains.fortran.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as DefColors
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.options.colors.AttributesDescriptor


enum class FortranHighlightingColors (humanName: String, val default: TextAttributesKey ) {
    IDENTIFIER("Identifier", DefColors.IDENTIFIER),
    LINE_COMMENT("Comment", DefColors.LINE_COMMENT),
    KEYWORD("Keyword", DefColors.KEYWORD),

    // literals
    INTEGER_LITERAL("Integer literal", DefColors.NUMBER),
    FLOATING_POINT_LITERAL("Floating point literal", DefColors.NUMBER),
    BINARY_LITERAL("Binary literal", DefColors.STRING),
    OCTAL_LITERAL("Octal literal", DefColors.STRING),
    HEX_LITERAL("Hex literal", DefColors.STRING),
    STRING_LITERAL("String literal", DefColors.STRING),
    LOGICAL_LITERAL("Logical literal", DefColors.KEYWORD),

    // operators
    ASSIGN_OPERATOR("Assignment operator", DefColors.OPERATION_SIGN),
    ARITHMETIC_OPERATOR("Arithmetic operator", DefColors.OPERATION_SIGN),
    RELATION_OPERATOR("Relation operator", DefColors.KEYWORD),
    LOGICAL_OPERATOR("Logical operator", DefColors.KEYWORD),
    DEFINED_OPERATOR("Defined operator", DefColors.KEYWORD),

    // parenthesis etc
    PARENTHESIS("Parenthesis", DefColors.PARENTHESES),
    BRACKETS("Brackets", DefColors.BRACKETS),
    ARRAY_CONSTRUCTOR("Array constructor", DefColors.BRACES),

    // punctuation
    PERCENTAGE("Percentage", DefColors.DOT),
    COMMA("Comma", DefColors.COMMA),
    COLON("Colon", DefColors.COMMA),

    BAD_CHARACTER("Bad character", HighlighterColors.BAD_CHARACTER);

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("org.jetbrains.fortran.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}
