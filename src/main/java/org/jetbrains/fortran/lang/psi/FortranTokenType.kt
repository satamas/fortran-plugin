package org.jetbrains.fortran.lang.psi

import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes.*
import java.util.*

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

        val KEYWORDS = TokenSet.create(KEYWORD, INCLUDE_KEYWORD, CHARACTER_KEYWORD, LOGICAL_KEYWORD, COMPLEX_KEYWORD,
                INTEGER_KEYWORD, REAL_KEYWORD, DOUBLE_KEYWORD, PRECISION_KEYWORD)

        var WHITE_SPACES = TokenSet.create(WHITE_SPACE, FIRST_WHITE_SPACE)

        var COMMENTS = TokenSet.create(LINE_COMMENT, LINE_CONTINUE, CONDITIONALLY_NON_COMPILED_COMMENT)

        val DIRECTIVES = TokenSet.create(DEFINE_DIRECTIVE, UNDEFINE_DIRECTIVE, IF_DEFINED_DIRECTIVE,
                IF_NOT_DEFINED_DIRECTIVE, ELSE_DIRECTIVE, ELIF_DIRECTIVE, ENDIF_DIRECTIVE, UNKNOWN_DIRECTIVE)

        val IF_DIRECTIVES = TokenSet.create(IF_DEFINED_DIRECTIVE, IF_NOT_DEFINED_DIRECTIVE)
        val END_IF_DIRECTIVES = TokenSet.create(ENDIF_DIRECTIVE, ELSE_DIRECTIVE, ELIF_DIRECTIVE)

        var STRINGS = TokenSet.create(STRINGLITERAL)

        fun keyword(name: String): IElementType {
            val keyword = FortranTokenType(name)
            keywords[name] = keyword
            return keyword
        }

        fun getKeyword(name: String) = keywords[name]
    }
}