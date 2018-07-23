package org.jetbrains.fortran.lang.lexer

import com.intellij.lang.ForeignLeafType
import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerUtil
import com.intellij.lexer.LookAheadLexer
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.indexing.IndexingDataKeys
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranFile
import org.jetbrains.fortran.lang.psi.FortranIncludeForeignLeafType
import org.jetbrains.fortran.lang.psi.FortranTokenType
import org.jetbrains.fortran.lang.resolveIncludedFile

class FortranIncludeProcessingLexer(val file: FortranFile?, val project: Project) : LookAheadLexer(FortranLexer(false)) {
    override fun lookAhead(baseLexer: Lexer) {
        val baseToken = baseLexer.tokenType
        if (baseToken == FortranTokenType.INCLUDE_KEYWORD) {
            processIncludeStatement(baseLexer)
        } else {
            addToken(baseToken)
            baseLexer.advance()
        }
    }

    private fun processIncludeStatement(baseLexer: Lexer) {
        ProgressManager.checkCanceled()
        addToken(baseLexer.tokenType)
        baseLexer.advance()
        skipWhiteSpaces(baseLexer)
        val tokenType = baseLexer.tokenType
        if (tokenType == FortranTypes.STRINGLITERAL) {
            val path = LexerUtil.getTokenText(baseLexer).toString().trim('"', '\'', ' ')
            val includedFile = resolveIncludedFile(getVirtualFile(file), path, project)

            addToken(baseLexer.tokenType)
            baseLexer.advance()
            skipWhiteSpaces(baseLexer)
            if (FortranTypes.EOL == baseLexer.tokenType) {
                addToken(FortranTypes.EOL)
                baseLexer.advance()
            }

            if (includedFile != null && includedFile.isValid) {
                val resolved = file?.manager?.findFile(includedFile)
                if (resolved != null) {
                    val substLexer = FortranIncludeProcessingLexer(file, project)
                    substLexer.start(resolved.text)

                    var previousWhitespace = false
                    while (true) {
                        val type = substLexer.tokenType ?: break
                        if (type is ForeignLeafType) {
                            addToken(baseLexer.tokenStart, type)
                            previousWhitespace = false
                        } else if (!FortranTokenType.WHITE_SPACES.contains(type)) {
                            val tokenText = LexerUtil.getTokenText(substLexer)
                            addToken(baseLexer.tokenStart, FortranIncludeForeignLeafType(type, tokenText))
                            previousWhitespace = false
                        } else if (!previousWhitespace) {
                            addToken(baseLexer.tokenStart, FortranIncludeForeignLeafType(TokenType.WHITE_SPACE, " "))
                            previousWhitespace = true
                        }
                        substLexer.advance()
                    }
                }
                addToken(baseLexer.tokenStart, FortranIncludeForeignLeafType(FortranTypes.EOL, "\n"))
            }
        }
    }

    fun skipWhiteSpaces(baseLexer: Lexer) {
        var tokenType = baseLexer.tokenType
        while (FortranTokenType.WHITE_SPACES.contains(tokenType)) {
            addToken(tokenType)
            baseLexer.advance()
            tokenType = baseLexer.tokenType
        }
    }

    fun getVirtualFile(file: PsiFile?): VirtualFile? {
        if (file == null) return null
        val vFile = file.originalFile.virtualFile
        return vFile ?: file.getUserData(IndexingDataKeys.VIRTUAL_FILE)
    }
}