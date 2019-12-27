package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import org.jetbrains.fortran.lang.FortranTypes.IDENTIFIER
import org.jetbrains.fortran.lang.parser.FortranParserUtil.consumeToken
import org.jetbrains.fortran.lang.parser.FortranParserUtil.enter_section_
import org.jetbrains.fortran.lang.parser.FortranParserUtil.exit_section_
import org.jetbrains.fortran.lang.parser.FortranParserUtil.recursion_guard_
import org.jetbrains.fortran.lang.psi.FortranTokenSets
import org.jetbrains.fortran.lang.psi.FortranTokenType

class IdentifierParser : FortranParserUtil.Parser {
    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "Identifier")) return false
        var result: Boolean
        val marker = enter_section_(builder)
        result = consumeToken(builder, IDENTIFIER)
        if (!result) {
            val tokenType = builder.tokenType
            if (tokenType === FortranTokenType.WORD || FortranTokenSets.KEYWORDS.contains(tokenType)) {
                builder.remapCurrentToken(FortranParserUtil.cloneTTwithBase(tokenType, IDENTIFIER))
                result = consumeToken(builder, IDENTIFIER)
            }
        }
        exit_section_(builder, marker, null, result)
        return result
    }
}
