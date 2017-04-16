package org.jetbrains.fortran.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranHighLighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer() = FortranLexer()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
            pack(map(tokenType)?.textAttributesKey)
    companion object {
        fun map(tokenType: IElementType?) : FortranHighlightingColors? = when(tokenType) {
            IDENTIFIER -> FortranHighlightingColors.IDENTIFIER
            FortranTokenType.LINE_COMMENT -> FortranHighlightingColors.LINE_COMMENT

            INTEGERLITERAL -> FortranHighlightingColors.INTEGER_LITERAL
            FLOATINGPOINTLITERAL -> FortranHighlightingColors.FLOATING_POINT_LITERAL
            DOUBLEPRECISIONLITERAL -> FortranHighlightingColors.FLOATING_POINT_LITERAL
            BINARYLITERAL -> FortranHighlightingColors.BINARY_LITERAL
            OCTALLITERAL  -> FortranHighlightingColors.OCTAL_LITERAL
            HEXLITERAL    -> FortranHighlightingColors.HEX_LITERAL
            STRINGLITERAL -> FortranHighlightingColors.STRING_LITERAL
            TRUEKWD, FALSEKWD -> FortranHighlightingColors.LOGICAL_LITERAL

            EQ, POINTER_ASSMNT -> FortranHighlightingColors.ASSIGN_OPERATOR
            PLUS, MINUS, MUL, POWER, DIV, DIVDIV -> FortranHighlightingColors.ARITHMETIC_OPERATOR
            EQEQ, NEQ, LT, LE, GT, GE -> FortranHighlightingColors.RELATION_OPERATOR
            LOGICAL_EQ, LOGICAL_NEQ, AND, OR, NOT -> FortranHighlightingColors.LOGICAL_OPERATOR
            DEFOPERATOR -> FortranHighlightingColors.DEFINED_OPERATOR

            LPAR, RPAR -> FortranHighlightingColors.PARENTHESIS
            LBRACKET, RBRACKET -> FortranHighlightingColors.BRACKETS
            ARRAYLBR, ARRAYRBR -> FortranHighlightingColors.ARRAY_CONSTRUCTOR

            PERC -> FortranHighlightingColors.PERCENTAGE
            COMMA -> FortranHighlightingColors.COMMA
            COLON, COLONCOLON -> FortranHighlightingColors.COLON

            in FortranTokenType.KEYWORDS -> FortranHighlightingColors.KEYWORD

            TokenType.BAD_CHARACTER -> FortranHighlightingColors.BAD_CHARACTER
            else -> null
        }
    }
}
