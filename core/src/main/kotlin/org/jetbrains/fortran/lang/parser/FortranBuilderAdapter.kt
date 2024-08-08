package org.jetbrains.fortran.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lang.TokenWrapper
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType
import org.jetbrains.fortran.lang.psi.FortranTokenSets


class FortranBuilderAdapter(
        delegate: PsiBuilder, state_: GeneratedParserUtilBase.ErrorState, parser_: PsiParser
) : GeneratedParserUtilBase.Builder(delegate, state_, parser_) {
    override fun advanceLexer() {
        parseMacros()
        delegate.advanceLexer()
    }

    override fun getTokenText(): String? {
        val tokenType = macroAwareTokenType
        return if (tokenType is TokenWrapper) {
            tokenType.value
        } else {
            delegate.tokenText
        }
    }

    override fun getTokenType(): IElementType? {
        var tokenType = FortranParserUtil.getUnwrappedTokenType(macroAwareTokenType)
        while (FortranTokenSets.WHITE_SPACES.contains(tokenType) || FortranTokenSets.COMMENTS.contains(tokenType)) {
            advanceLexer()
            tokenType = FortranParserUtil.getUnwrappedTokenType(macroAwareTokenType)
        }
        return tokenType
    }

    override fun remapCurrentToken(type: IElementType) {
        delegate.remapCurrentToken(FortranParserUtil.cloneTTwithBase(macroAwareTokenType, type))
    }

    override fun eof(): Boolean {
        parseMacros()
        return delegate.eof()
    }

    override fun getCurrentOffset(): Int {
        parseMacros()
        return delegate.currentOffset
    }

    private val macroAwareTokenType: IElementType?
        get() {
            parseMacros()
            return delegate.tokenType
        }

    private fun parseMacros() {
        if (delegate.tokenType !in FortranTokenSets.DIRECTIVES) return
        val builder = GeneratedParserUtilBase.Builder(delegate, state, parser)
        FortranParser.macro(builder, 1)
    }
}