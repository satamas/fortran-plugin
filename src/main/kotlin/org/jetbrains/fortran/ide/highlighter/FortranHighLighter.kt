package org.jetbrains.fortran.ide.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranHighLighter(fFixedForm: Boolean) : SyntaxHighlighterBase() {
    private val keys = HashMap<IElementType, TextAttributesKey>()

    init {
        fillMap(keys, FortranTokenType.DIRECTIVES, FortranHighlightingColors.DIRECTIVE.textAttributesKey)
        keys[IDENTIFIER] = FortranHighlightingColors.IDENTIFIER.textAttributesKey
        keys[FortranTokenType.LINE_COMMENT] = FortranHighlightingColors.LINE_COMMENT.textAttributesKey
        keys[FortranTokenType.CONDITIONALLY_NON_COMPILED_COMMENT] = FortranHighlightingColors.LINE_COMMENT.textAttributesKey
        keys[FortranTokenType.LINE_CONTINUE] = FortranHighlightingColors.LINE_CONTINUE.textAttributesKey

        keys[DATAEDIT] = FortranHighlightingColors.INTEGER_LITERAL.textAttributesKey
        keys[INTEGERLITERAL] = FortranHighlightingColors.INTEGER_LITERAL.textAttributesKey

        keys[FLOATINGPOINTLITERAL] = FortranHighlightingColors.FLOATING_POINT_LITERAL.textAttributesKey
        keys[DOUBLEPRECISIONLITERAL] = FortranHighlightingColors.FLOATING_POINT_LITERAL.textAttributesKey

        keys[BINARYLITERAL] = FortranHighlightingColors.BINARY_LITERAL.textAttributesKey

        keys[OCTALLITERAL] = FortranHighlightingColors.OCTAL_LITERAL.textAttributesKey

        keys[HEXLITERAL] = FortranHighlightingColors.HEX_LITERAL.textAttributesKey

        keys[STRINGLITERAL] = FortranHighlightingColors.STRING_LITERAL.textAttributesKey
        keys[STRINGSTART] = FortranHighlightingColors.STRING_LITERAL.textAttributesKey
        keys[STRINGMIDDLE] = FortranHighlightingColors.STRING_LITERAL.textAttributesKey
        keys[STRINGEND] = FortranHighlightingColors.STRING_LITERAL.textAttributesKey

        keys[TRUEKWD] = FortranHighlightingColors.LOGICAL_LITERAL.textAttributesKey
        keys[FALSEKWD] = FortranHighlightingColors.LOGICAL_LITERAL.textAttributesKey

        keys[EQ] = FortranHighlightingColors.ASSIGN_OPERATOR.textAttributesKey
        keys[POINTER_ASSMNT] = FortranHighlightingColors.ASSIGN_OPERATOR.textAttributesKey

        keys[PLUS] = FortranHighlightingColors.ARITHMETIC_OPERATOR.textAttributesKey
        keys[MINUS] = FortranHighlightingColors.ARITHMETIC_OPERATOR.textAttributesKey
        keys[MUL] = FortranHighlightingColors.ARITHMETIC_OPERATOR.textAttributesKey
        keys[POWER] = FortranHighlightingColors.ARITHMETIC_OPERATOR.textAttributesKey
        keys[DIV] = FortranHighlightingColors.ARITHMETIC_OPERATOR.textAttributesKey
        keys[DIVDIV] = FortranHighlightingColors.ARITHMETIC_OPERATOR.textAttributesKey

        keys[EQEQ] = FortranHighlightingColors.RELATION_OPERATOR.textAttributesKey
        keys[NEQ] = FortranHighlightingColors.RELATION_OPERATOR.textAttributesKey
        keys[LT] = FortranHighlightingColors.RELATION_OPERATOR.textAttributesKey
        keys[LE] = FortranHighlightingColors.RELATION_OPERATOR.textAttributesKey
        keys[GT] = FortranHighlightingColors.RELATION_OPERATOR.textAttributesKey
        keys[GE] = FortranHighlightingColors.RELATION_OPERATOR.textAttributesKey

        keys[LOGICAL_EQ] = FortranHighlightingColors.LOGICAL_OPERATOR.textAttributesKey
        keys[LOGICAL_NEQ] = FortranHighlightingColors.LOGICAL_OPERATOR.textAttributesKey
        keys[AND] = FortranHighlightingColors.LOGICAL_OPERATOR.textAttributesKey
        keys[OR] = FortranHighlightingColors.LOGICAL_OPERATOR.textAttributesKey
        keys[NOT] = FortranHighlightingColors.LOGICAL_OPERATOR.textAttributesKey

        keys[DEFOPERATOR] = FortranHighlightingColors.DEFINED_OPERATOR.textAttributesKey

        keys[LPAR] = FortranHighlightingColors.PARENTHESIS.textAttributesKey
        keys[RPAR] = FortranHighlightingColors.PARENTHESIS.textAttributesKey

        keys[LBRACKET] = FortranHighlightingColors.BRACKETS.textAttributesKey
        keys[RBRACKET] = FortranHighlightingColors.BRACKETS.textAttributesKey

        keys[ARRAYLBR] = FortranHighlightingColors.ARRAY_CONSTRUCTOR.textAttributesKey
        keys[ARRAYRBR] = FortranHighlightingColors.ARRAY_CONSTRUCTOR.textAttributesKey

        keys[PERC] = FortranHighlightingColors.PERCENTAGE.textAttributesKey

        keys[COMMA] = FortranHighlightingColors.COMMA.textAttributesKey

        keys[COLON] = FortranHighlightingColors.COLON.textAttributesKey
        keys[COLONCOLON] = FortranHighlightingColors.COLON.textAttributesKey

        keys[FortranTokenType.FIRST_WHITE_SPACE] = FortranHighlightingColors.FIRST_WHITE_SPACE.textAttributesKey

        keys[TokenType.BAD_CHARACTER] = FortranHighlightingColors.BAD_CHARACTER.textAttributesKey
    }

    val fFixedForm_ = fFixedForm

    override fun getHighlightingLexer() = FortranLexer(fFixedForm_)

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = pack(keys[tokenType])
}
