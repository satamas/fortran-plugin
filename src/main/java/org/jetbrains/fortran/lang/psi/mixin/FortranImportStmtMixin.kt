package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerUtil
import com.intellij.openapi.util.SystemInfo
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import org.jetbrains.fortran.lang.psi.FortranIncludeStmt
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl

abstract class FortranIncludeStmtMixin(node: ASTNode) : FortranCompositeElementImpl(node), FortranIncludeStmt {
//    override fun getReferences(): Array<out PsiReference?> {
//        val fileNameElement = stringliteral ?: return emptyArray()
//        val fileName = fileNameElement.text.trim('\'', '"')
//        val fileReferenceSet = FileReferenceSet(
//                fileName, this, fileNameElement.startOffsetInParent + 1, null,
//                SystemInfo.isFileSystemCaseSensitive, true)
//        return fileReferenceSet.allReferences
//    }
//
//    protected override fun lookAhead(baseLexer: Lexer) {
//        addAllTokensAsSynthetic(myPrefixLexer, 0)

//        val baseToken = baseLexer.tokenType
//        val altCppP: String
//        if (baseToken === IDENTIFIER || KEYWORDS.contains(baseToken)
//                || (altCppP = OCElementUtil.getAlternativeCppPunctuator(baseToken)) != null && altCppP.contentEquals(LexerUtil.getTokenText(baseLexer))) {
//
//            val id = LexerUtil.getTokenText(baseLexer).toString()
//
//            if (myEvalDefinedFunction && "defined" == id) {
//                processDefinedFunction(baseLexer)
//            } else if (myEvalDefinedFunction && "__has_include" == id) {
//                processHasInclude(baseLexer, false)
//            } else if (myEvalDefinedFunction && "__has_include_next" == id) {
//                processHasInclude(baseLexer, true)
//            } else {
//                if (!myInsideIfCondition && myEvalDefinedFunction) {
//                     when evaluating #if contents notify myState about potential macro dependency
//                     getDefinition() following this won't help since it doesn't store "not defined" state
//                    myState.isDefined(id)
//                }
//                var macro = myState.getDefinition(id)
//                if (macro == null) {
//                    val numStr = lineNumberFromStub(id)
//                    if (numStr != null) {
//                        macro = OCMacroSymbol(myState.getProject(), null, -1, id, null, numStr!!)
//                    }
//                }
//
//                if (!myAtDefineDirective && macro != null) {
//                    processMacroSubstitution(baseLexer, macro!!, true)
//                } else {
//                    addToken(baseToken)
//                    baseLexer.advance()
//
//                    if (!myDontSubstituteInDefines)
//                        myAtDefineDirective = false
//
//                    updateBraceStack(baseToken, id)
//                }
//            }
//        } else if (baseToken === DEFINE_DIRECTIVE) {
//            processDefineDirective(baseLexer)
//        } else if (baseToken === UNDEF_DIRECTIVE) {
//            processUndefineDirective(baseLexer)
//        } else if (baseToken === INCLUDE_DIRECTIVE) {
//            processIncludeDirective(baseLexer, false, false)
//        } else if (baseToken === IMPORT_DIRECTIVE) {
//            processIncludeDirective(baseLexer, true, false)
//        } else if (baseToken === INCLUDE_NEXT_DIRECTIVE) {
//            processIncludeDirective(baseLexer, false, true)
//        } else if (baseToken === IF_DIRECTIVE) {
//            processIfDirective(baseLexer, IF_EVAL)
//        } else if (baseToken === IFDEF_DIRECTIVE) {
//            processIfDirective(baseLexer, IFDEF_EVAL)
//        } else if (baseToken === IFNDEF_DIRECTIVE) {
//            processIfDirective(baseLexer, IFNDEF_EVAL)
//        } else if (baseToken === PRAGMA_DIRECTIVE) {
//            processBaseDirective(baseLexer, OCLexer.PRAGMA_BODY_STATE)
//        } else if (DIRECTIVES.contains(baseToken)) {
//            processBaseDirective(baseLexer, OCLexer.DIRECTIVE_BODY_STATE)
//        } else {
//            if (baseToken === RBRACE) {
//                emitCounterDefinitionIfRequired(baseLexer)
//            } else if (WHITE_SPACE_OR_COMMENT_BIT_SET.contains(baseToken)) {
//                val hasNewLines = adjustLineCount(LexerUtil.getTokenText(baseLexer))
//                if (hasNewLines) {
//                    emitCounterDefinitionIfRequired(baseLexer)
//                }
//            }
//
//            updateBraceStack(baseToken, null)
//            super.lookAhead(baseLexer)
//        }
//
//        if (baseLexer.tokenType == null) {
//            emitCounterDefinitionIfRequired(baseLexer)
//            addAllTokensAsSynthetic(mySuffixLexer, -1)
//        }
//    }
}