package org.jetbrains.fortran.lang.preprocessor

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.lexer.LexerUtil
import com.intellij.lexer.LookAheadLexer
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranPreprocessingLexer : LookAheadLexer(FortranLexer(false)) {
    private val macrosContext: FortranMacrosContext = FortranMacrosContextImpl()

    override fun lookAhead(baseLexer: Lexer) {

        val directives = TokenSet.create(DEFINE_DIRECTIVE, UNDEFINE_DIRECTIVE, IF_DIRECTIVE, IF_DEFINED_DIRECTIVE,
                IF_NOT_DEFINED_DIRECTIVE, ELSE_DIRECTIVE, ELIF_DIRECTIVE, ENDIF_DIRECTIVE, UNKNOWN_DIRECTIVE)

        when (val baseToken = baseLexer.tokenType){
            in directives -> processDirective(baseLexer)
            else -> {
                if (macrosContext.inEvaluatedContext() || baseLexer.tokenType == null) {
                    addToken(baseToken)
                    baseLexer.advance()
                } else {
                    skipNonDirectiveContent(baseLexer)
                }

            }
        }
    }

    private fun skipNonDirectiveContent(baseLexer: Lexer) {
        val directives = TokenSet.create(DEFINE_DIRECTIVE, UNDEFINE_DIRECTIVE, IF_DIRECTIVE, IF_DEFINED_DIRECTIVE,
                IF_NOT_DEFINED_DIRECTIVE, ELSE_DIRECTIVE, ELIF_DIRECTIVE, ENDIF_DIRECTIVE, UNKNOWN_DIRECTIVE)
        assert(baseLexer.tokenType !in directives)
        var beforeEnd: LexerPosition? = null
        while (baseLexer.tokenType != null && baseLexer.tokenType !in directives) {
            beforeEnd = baseLexer.currentPosition
            baseLexer.advance()
        }
        if (beforeEnd != null) {
            baseLexer.restore(beforeEnd)
            advanceAs(baseLexer, FortranTokenType.CONDITIONALLY_NON_COMPILED_COMMENT)
        }
    }

    private fun processDirective(baseLexer: Lexer) {
        ProgressManager.checkCanceled()

        // consume `#macro` part
        val directive = baseLexer.tokenType
        advanceLexer(baseLexer)

        if (directive is FortranTokenType) {

            // consume macro content
            val content = {
                if (baseLexer.tokenType == DIRECTIVE_CONTENT) {
                    val result =  LexerUtil.getTokenText(baseLexer).toString()
                    advanceLexer(baseLexer)
                    result
                } else {
                    ""
                }
            }()
            // alter context
            macrosContext.add(FortranMacro(directive, content))
        }
    }


}