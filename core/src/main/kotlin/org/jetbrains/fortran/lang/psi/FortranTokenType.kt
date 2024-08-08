package org.jetbrains.fortran.lang.psi

import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes

/**
 * Created by sergei on 13.03.17.
 * to have keywords highlighting
 * parameters type keywords must be deleted from here
 */
class FortranTokenType(debug: String) : IElementType(debug, FortranLanguage) {
    companion object {
        private val keywords = HashMap<String, IElementType>()

        @JvmField
        val LINE_COMMENT: IElementType = FortranTokenType("comment")
        val CONDITIONALLY_NON_COMPILED_COMMENT: IElementType = FortranTokenType("conditionally_non_compiled_comment")

        @JvmField
        val FIRST_WHITE_SPACE: IElementType = FortranTokenType("white_space")

        @JvmField
        val LINE_CONTINUE: IElementType = FortranTokenType("line_continue")

        @JvmField
        val WORD: IElementType = FortranTokenType("identifier_or_keyword")

        @JvmField
        val KEYWORD: IElementType = FortranTokenType("keyword")

        @JvmField
        val INCLUDE_KEYWORD = keyword("include")
        @JvmField
        val CHARACTER_KEYWORD = keyword("character")
        @JvmField
        val LOGICAL_KEYWORD = keyword("logical")
        @JvmField
        val COMPLEX_KEYWORD = keyword("complex")
        @JvmField
        val INTEGER_KEYWORD = keyword("integer")
        @JvmField
        val REAL_KEYWORD = keyword("real")

        @JvmField
        val DOUBLE_KEYWORD = keyword("double")
        @JvmField
        val PRECISION_KEYWORD = keyword("precision")

        fun keyword(name: String): IElementType {
            val keyword = FortranTokenType(name)
            keywords[name] = keyword
            return keyword
        }

        fun getKeyword(name: String) = keywords[name]
    }
}

object FortranTokenSets {
    val KEYWORDS = TokenSet.create(FortranTokenType.KEYWORD, FortranTokenType.INCLUDE_KEYWORD, FortranTokenType.CHARACTER_KEYWORD, FortranTokenType.LOGICAL_KEYWORD, FortranTokenType.COMPLEX_KEYWORD,
            FortranTokenType.INTEGER_KEYWORD, FortranTokenType.REAL_KEYWORD, FortranTokenType.DOUBLE_KEYWORD, FortranTokenType.PRECISION_KEYWORD)

    var WHITE_SPACES = TokenSet.create(WHITE_SPACE, FortranTokenType.FIRST_WHITE_SPACE)

    var COMMENTS = TokenSet.create(FortranTokenType.LINE_COMMENT, FortranTokenType.LINE_CONTINUE, FortranTokenType.CONDITIONALLY_NON_COMPILED_COMMENT)

    val DIRECTIVES = TokenSet.create(
            FortranTypes.DEFINE_DIRECTIVE, FortranTypes.UNDEFINE_DIRECTIVE,
            FortranTypes.IF_DIRECTIVE, FortranTypes.IF_DEFINED_DIRECTIVE,
            FortranTypes.IF_NOT_DEFINED_DIRECTIVE, FortranTypes.ELSE_DIRECTIVE,
            FortranTypes.ELIF_DIRECTIVE, FortranTypes.ENDIF_DIRECTIVE,
            FortranTypes.UNKNOWN_DIRECTIVE
    )

    val IF_DIRECTIVES = TokenSet.create(
            FortranTypes.IF_DIRECTIVE, FortranTypes.IF_DEFINED_DIRECTIVE, FortranTypes.IF_NOT_DEFINED_DIRECTIVE
    )
    val END_IF_DIRECTIVES = TokenSet.create(
            FortranTypes.ENDIF_DIRECTIVE, FortranTypes.ELSE_DIRECTIVE, FortranTypes.ELIF_DIRECTIVE
    )

    var STRINGS = TokenSet.create(FortranTypes.STRINGLITERAL)
}