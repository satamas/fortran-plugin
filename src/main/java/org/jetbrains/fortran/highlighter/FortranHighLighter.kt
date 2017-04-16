package org.jetbrains.fortran.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType;
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranHighLighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer() = FortranLexer()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
            pack(map(tokenType)?.textAttributesKey)
    companion object {
        fun map(tokenType: IElementType?) : FortranHighlightingColors? = when(tokenType) {
            FortranTokenType.LINE_COMMENT -> FortranHighlightingColors.LINE_COMMENT
            STRINGLITERAL -> FortranHighlightingColors.STRING
            BINARYLITERAL -> FortranHighlightingColors.STRING
            OCTALLITERAL  -> FortranHighlightingColors.STRING
            HEXLITERAL    -> FortranHighlightingColors.STRING

            INTEGERLITERAL -> FortranHighlightingColors.NUMBER
            FLOATINGPOINTLITERAL -> FortranHighlightingColors.NUMBER
            DOUBLEPRECISIONLITERAL -> FortranHighlightingColors.NUMBER

            in FortranTokenType.KEYWORDS -> FortranHighlightingColors.KEYWORD
            in FortranTokenType.BOOL_LITERAL -> FortranHighlightingColors.KEYWORD

            TokenType.BAD_CHARACTER -> FortranHighlightingColors.BAD_CHARACTER
            else -> null
        }
    }
}
