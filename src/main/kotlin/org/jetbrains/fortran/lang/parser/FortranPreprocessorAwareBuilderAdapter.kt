package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import org.jetbrains.fortran.lang.psi.FortranTokenSets


class FortranPreprocessorAwareBuilderAdapter(
        delegate: PsiBuilder, state_: GeneratedParserUtilBase.ErrorState, parser_: PsiParser
) : GeneratedParserUtilBase.Builder(delegate, state_, parser_) {
    private fun parseMacros() {
        val builder = GeneratedParserUtilBase.Builder(delegate, state, parser)
        if (builder.tokenType !in FortranTokenSets.DIRECTIVES) return
        FortranParser.macro(builder, 1)
    }

    override fun advanceLexer() {
        parseMacros()
        super.advanceLexer()
    }

    override fun getTokenText(): String? {
        parseMacros()
        return super.getTokenText()
    }

    override fun getTokenType(): IElementType? {
        parseMacros()
        return super.getTokenType()
    }

    override fun eof(): Boolean {
        parseMacros()
        return super.eof()
    }

    override fun getCurrentOffset(): Int {
        parseMacros()
        return super.getCurrentOffset()
    }
}