package org.jetbrains.fortran.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

/**
 * Created by Sergei on 13.04.17.
 * The class for manual parsing of the external rules in the bnf grammar
 * This is a copy of generated grammar to test, how it compiles
 */

object FortranParserUtil : GeneratedParserUtilBase() {
    @JvmStatic
    fun parseIdentifier(builder: PsiBuilder, level: Int): Boolean {
        return IdentifierParser().parse(builder, level)
    }

    @JvmStatic
    fun parseKeyword(builder: PsiBuilder, level: Int, keyword: String): Boolean {
        return KeywordParser(keyword).parse(builder, level)
    }

    @JvmStatic
    fun parseLabeledDoConstruct(builder: PsiBuilder, level: Int): Boolean {
        return LabeledDoConstructParser().parse(builder, level)
    }

    @JvmStatic
    fun parseLabelOrExpression(builder: PsiBuilder, level: Int): Boolean {
        return LabelOrExpressionParser().parse(builder, level)
    }
}
