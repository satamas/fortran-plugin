package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import org.jetbrains.fortran.lang.FortranTypes.IDENTIFIER
import org.jetbrains.fortran.lang.parser.FortranParserUtil.consumeToken
import org.jetbrains.fortran.lang.parser.FortranParserUtil.enter_section_
import org.jetbrains.fortran.lang.parser.FortranParserUtil.exit_section_
import org.jetbrains.fortran.lang.parser.FortranParserUtil.recursion_guard_
import org.jetbrains.fortran.lang.psi.FortranTokenType

class KeywordParser(private val keyword_text: String) : FortranParserUtil.Parser {
    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "Identifier")) return false
        val expectedType = FortranTokenType.getKeyword(keyword_text.toLowerCase()) ?: FortranTokenType.KEYWORD
        var result = false
        val marker = enter_section_(builder)
        val tokenType = builder.tokenType
        if (tokenType === IDENTIFIER || tokenType === FortranTokenType.WORD || FortranTokenType.KEYWORDS.contains(tokenType)) {
            if (keyword_text.equals(builder.tokenText!!, ignoreCase = true)) {
                builder.remapCurrentToken(FortranParserUtil.cloneTTwithBase(tokenType, expectedType))
                result = consumeToken(builder, expectedType)
            }
        }
        exit_section_(builder, marker, null, result)
        return result
    }
}


