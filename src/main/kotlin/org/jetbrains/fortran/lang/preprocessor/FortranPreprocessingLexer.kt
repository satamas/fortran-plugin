package org.jetbrains.fortran.lang.preprocessor

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.lexer.LexerUtil
import com.intellij.lexer.LookAheadLexer
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.text.StringUtil
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranPreprocessingLexer : LookAheadLexer(FortranLexer(false)) {
    private val macrosContext: FortranMacrosContext = FortranMacrosContextImpl()

    private val ifDefinedDecisionEvaluator = { content: CharSequence ->
        val words = StringUtil.getWordsIn(content.toString())
        val identifier = if (words.isEmpty()) content.toString().trim() else words[0]
        macrosContext.isDefined(identifier)
    }
    private val ifNotDefinedDecisionEvaluator = { name: CharSequence -> !ifDefinedDecisionEvaluator(name) }

    override fun lookAhead(baseLexer: Lexer) {
        when (val baseToken = baseLexer.tokenType) {
            DEFINE_DIRECTIVE -> processDefineDirective(baseLexer)
            UNDEFINE_DIRECTIVE -> processUndefineDirective(baseLexer)
            IF_DEFINED_DIRECTIVE -> processIfDirective(baseLexer, ifDefinedDecisionEvaluator)
            IF_NOT_DEFINED_DIRECTIVE -> processIfDirective(baseLexer, ifNotDefinedDecisionEvaluator)
            else -> {
                addToken(baseToken)
                baseLexer.advance()
            }
        }
    }

    private fun processDefineDirective(baseLexer: Lexer) {
        ProgressManager.checkCanceled()
        advanceLexer(baseLexer)

        val tokenType = baseLexer.tokenType
        if (tokenType == DIRECTIVE_CONTENT) {
            val content = LexerUtil.getTokenText(baseLexer)
            val def = FortranMacro.parseFromDirectiveContent(content)
            advanceLexer(baseLexer)
            if (def != null) {
                macrosContext.define(def)
            }
        }
    }

    private fun processUndefineDirective(baseLexer: Lexer) {
        advanceLexer(baseLexer)
        val tokenType = baseLexer.tokenType
        if (tokenType == DIRECTIVE_CONTENT) {
            val contents = LexerUtil.getTokenText(baseLexer)
            advanceLexer(baseLexer)

            val lexer = FortranLexer(false)
            lexer.start(contents)
            while (FortranTokenType.WHITE_SPACES.contains(lexer.tokenType)) {
                lexer.advance()
            }

            if (lexer.tokenType == FortranTokenType.WORD) {
                macrosContext.undefine(LexerUtil.getTokenText(lexer).toString())
            }
        }
    }

    private fun processIfDirective(lexer: Lexer, decisionEvaluator: (input: CharSequence) -> Boolean) {
        advanceLexer(lexer)
        if (lexer.tokenType != DIRECTIVE_CONTENT) return
        val content = LexerUtil.getTokenText(lexer)
        val decision = decisionEvaluator(content)

        advanceLexer(lexer)

        if (decision) {
            processConditionals(lexer)
            val tt = lexer.tokenType
            if (tt == ELSE_DIRECTIVE || tt == ELIF_DIRECTIVE) {
                skipDirectiveWithContent(lexer)
                skipConditionals(lexer, false)
            }
        } else {
            skipConditionals(lexer, true)

            val tt = lexer.tokenType
            if (tt == ELSE_DIRECTIVE || tt == ELIF_DIRECTIVE) {
                skipDirectiveWithContent(lexer)

                processConditionals(lexer)
            }
        }

        if (lexer.tokenType === ENDIF_DIRECTIVE) advanceLexer(lexer)
    }

    private fun processConditionals(lexer: Lexer) {
        var tokenType = lexer.tokenType
        while (tokenType != null && tokenType !in FortranTokenType.END_IF_DIRECTIVES) {
            lookAhead(lexer)
            tokenType = lexer.tokenType
        }
    }

    private fun skipDirectiveWithContent(lexer: Lexer) {
        advanceLexer(lexer)
        if (lexer.tokenType == DIRECTIVE_CONTENT) {
            advanceLexer(lexer)
        }
    }

    private fun skipConditionals(lexer: Lexer, stopOnElse: Boolean) {
        var nesting = 1

        while (lexer.tokenType in FortranTokenType.WHITE_SPACES) advanceLexer(lexer)

        var beforeEnd: LexerPosition? = null
        while (true) {
            val tokenType = lexer.tokenType ?: break

            if (tokenType in FortranTokenType.IF_DIRECTIVES) {
                nesting++
            } else if ((stopOnElse && (tokenType == ELIF_DIRECTIVE || tokenType == ELSE_DIRECTIVE)) || tokenType == ENDIF_DIRECTIVE) {
                nesting--
                if (nesting == 0) break
                if (tokenType !== ENDIF_DIRECTIVE) nesting++
            }

            beforeEnd = lexer.currentPosition
            lexer.advance()
        }

        if (beforeEnd != null) {
            lexer.restore(beforeEnd)
            advanceAs(lexer, FortranTokenType.CONDITIONALLY_NON_COMPILED_COMMENT)
        }

        while (lexer.tokenType in FortranTokenType.WHITE_SPACES) advanceLexer(lexer)
    }
}