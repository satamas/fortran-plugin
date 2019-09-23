package org.jetbrains.fortran.lang.preprocessor

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.lexer.LexerUtil
import com.intellij.lexer.LookAheadLexer
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.tree.TokenSet
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
    private val ifDecisionEvaluator = { directive_content: CharSequence -> !directive_content.endsWith("/* macro_eval: false */") }

    override fun lookAhead(baseLexer: Lexer) {
        val CONDITION_DIRECTIVES = TokenSet.create(IF_DIRECTIVE, IF_DEFINED_DIRECTIVE, IF_NOT_DEFINED_DIRECTIVE, ELSE_DIRECTIVE, ELIF_DIRECTIVE, ENDIF_DIRECTIVE)

        when (val baseToken = baseLexer.tokenType) {
            DEFINE_DIRECTIVE -> processDefineDirective(baseLexer)
            UNDEFINE_DIRECTIVE -> processUndefineDirective(baseLexer)
            IF_DEFINED_DIRECTIVE -> processIfDirective(baseLexer, ifDefinedDecisionEvaluator)
            IF_NOT_DEFINED_DIRECTIVE -> processIfDirective(baseLexer, ifNotDefinedDecisionEvaluator)
            IF_DIRECTIVE -> processIfDirective(baseLexer, ifDecisionEvaluator)
            ELSE_DIRECTIVE -> processElseDirective(baseLexer)
            ELIF_DIRECTIVE -> processElseIfDirective(baseLexer)
            ENDIF_DIRECTIVE -> processEndIfDirective(baseLexer)
            else -> {
                if (macrosContext.inEvaluatedContext() || baseLexer.tokenType == null) {
                    addToken(baseToken)
                    baseLexer.advance()
                } else {
                    skipNonDirectiveContent(baseLexer, CONDITION_DIRECTIVES)
                }

            }
        }
    }

    private fun skipNonDirectiveContent(baseLexer: Lexer, DIRECTIVES: TokenSet) {
        assert(baseLexer.tokenType !in DIRECTIVES)
        var beforeEnd: LexerPosition? = null
        while (baseLexer.tokenType != null && baseLexer.tokenType !in DIRECTIVES) {
            beforeEnd = baseLexer.currentPosition
            baseLexer.advance()
        }
        if (beforeEnd != null) {
            baseLexer.restore(beforeEnd)
            advanceAs(baseLexer, FortranTokenType.CONDITIONALLY_NON_COMPILED_COMMENT)
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
        var decision = true
        if (lexer.tokenType == DIRECTIVE_CONTENT) {
            val content = LexerUtil.getTokenText(lexer)
            advanceLexer(lexer)
            decision = decisionEvaluator(content)
        }
        macrosContext.enterIf(decision)
    }

    private fun processElseDirective(lexer: Lexer) {
        advanceLexer(lexer)
        macrosContext.enterElse()
    }

    private fun processEndIfDirective(lexer: Lexer) {
        advanceLexer(lexer)
        macrosContext.exitIf()
    }

    private fun processElseIfDirective(lexer: Lexer) {
        advanceLexer(lexer)
        var decision = true
        if (lexer.tokenType == DIRECTIVE_CONTENT) {
            val content = LexerUtil.getTokenText(lexer)
            advanceLexer(lexer)
            decision = ifDecisionEvaluator(content)
        }
        macrosContext.enterElseIf(decision)
    }

}