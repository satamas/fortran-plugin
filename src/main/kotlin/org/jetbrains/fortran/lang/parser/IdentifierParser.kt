package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase.*
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType.WORD
import org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORD

class IdentifierParser : Parser {
    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "Identifier")) return false
        var result: Boolean
        val marker = enter_section_(builder)
        result = consumeToken(builder, IDENTIFIER)
        if (!result) {
            if (builder.tokenType === WORD || builder.tokenType === KEYWORD) {
                builder.remapCurrentToken(IDENTIFIER)
                result = consumeToken(builder, IDENTIFIER)
            }
        }
        exit_section_(builder, marker, null, result)
        return result
    }
}
