package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase.*
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType
import org.jetbrains.fortran.lang.psi.FortranTokenType.*

class KeywordParser(private val keyword_text: String) : Parser {
    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "Identifier")) return false
        var result = false
        val marker = enter_section_(builder)
        if (builder.tokenType === IDENTIFIER || builder.tokenType === WORD
                || KEYWORDS.contains(builder.tokenType)) {
            if (keyword_text.equals(builder.tokenText!!, ignoreCase = true)) {
                val expectedType = FortranTokenType.getKeyword(keyword_text.toLowerCase()) ?: KEYWORD
                builder.remapCurrentToken(expectedType)
                result = consumeToken(builder, expectedType)
            }
        }
        exit_section_(builder, marker, null, result)
        return result
    }
}

