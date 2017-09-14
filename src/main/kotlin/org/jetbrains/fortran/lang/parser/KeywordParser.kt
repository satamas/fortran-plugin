package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase.*
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORD
import org.jetbrains.fortran.lang.psi.FortranTokenType.WORD

class KeywordParser(private val keyword_text: String) : Parser {
    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "Identifier")) return false
        var result = false
        val marker = enter_section_(builder)
        if (builder.tokenType === IDENTIFIER || builder.tokenType === WORD
                || builder.tokenType === KEYWORD) {
            if (keyword_text.equals(builder.tokenText!!, ignoreCase = true)) {
                builder.remapCurrentToken(KEYWORD)
                result = consumeToken(builder, KEYWORD)
            }
        }
        exit_section_(builder, marker, null, result)
        return result
    }
}

