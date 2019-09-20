package org.jetbrains.fortran.ide.highlighter

import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as DefColors


enum class FortranHighlightingColors (humanName: String, val default: TextAttributesKey ) {
    IDENTIFIER("Identifier", DefColors.IDENTIFIER),
    LINE_COMMENT("Comment", DefColors.LINE_COMMENT),
    CONDITIONALLY_NOT_COMPILED("Conditionally not compiled", DefColors.BLOCK_COMMENT),
    LINE_CONTINUE("Line continue sign", DefColors.LINE_COMMENT),
    KEYWORD("Keyword", DefColors.KEYWORD),

    // literals
    INTEGER_LITERAL("Literals//Integer literal", DefColors.NUMBER),
    FLOATING_POINT_LITERAL("Literals//Floating point literal", DefColors.NUMBER),
    BINARY_LITERAL("Literals//Binary literal", DefColors.STRING),
    OCTAL_LITERAL("Literals//Octal literal", DefColors.STRING),
    HEX_LITERAL("Literals//Hex literal", DefColors.STRING),
    STRING_LITERAL("Literals//String literal", DefColors.STRING),
    LOGICAL_LITERAL("Literals//Logical literal", DefColors.KEYWORD),

    // operators
    ASSIGN_OPERATOR("Operators//Assignment operator", DefColors.OPERATION_SIGN),
    ARITHMETIC_OPERATOR("Operators//Arithmetic operator", DefColors.OPERATION_SIGN),
    RELATION_OPERATOR("Operators//Relation operator", DefColors.KEYWORD),
    LOGICAL_OPERATOR("Operators//Logical operator", DefColors.KEYWORD),
    DEFINED_OPERATOR("Operators//Defined operator", DefColors.KEYWORD),

    // parenthesis etc
    PARENTHESIS("Braces//Parenthesis", DefColors.PARENTHESES),
    BRACKETS("Braces//Brackets", DefColors.BRACKETS),
    ARRAY_CONSTRUCTOR("Braces//Array constructor", DefColors.BRACES),

    // punctuation
    PERCENTAGE("Percentage", DefColors.DOT),
    COMMA("Comma", DefColors.COMMA),
    COLON("Colon", DefColors.COMMA),

    // C preprocessor
    DIRECTIVE("Fortran preprocessor directive", DefColors.METADATA),

    FIRST_WHITE_SPACE("Fixed Form leading whitespace", EditorColors.FOLDED_TEXT_ATTRIBUTES),

    BAD_CHARACTER("Bad character", HighlighterColors.BAD_CHARACTER);


    val textAttributesKey = TextAttributesKey.createTextAttributesKey("org.jetbrains.fortran.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
}
