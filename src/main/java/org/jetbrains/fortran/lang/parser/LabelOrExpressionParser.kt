package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase

import com.intellij.lang.parser.GeneratedParserUtilBase.*
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType.WORD
import org.jetbrains.fortran.lang.parser.FortranParser.*

class LabelOrExpressionParser : GeneratedParserUtilBase.Parser {

    override fun parse(builder: PsiBuilder, level: Int): Boolean {
        if (!recursion_guard_(builder, level, "label or expression")) return false
        var result = false
        val marker = enter_section_(builder)
        result = expr(builder, level + 1, -1);

        exit_section_(builder, marker, null, result)
        return result
    }
}

