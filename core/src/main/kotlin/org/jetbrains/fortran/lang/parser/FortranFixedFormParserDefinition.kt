package org.jetbrains.fortran.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile
import org.jetbrains.fortran.lang.psi.FortranTokenSets
import org.jetbrains.fortran.lang.stubs.FortranFixedFormFileStub

class FortranFixedFormParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer {
        return FortranLexer(true)
    }

    override fun createParser(project: Project): PsiParser {
        return FortranParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FortranFixedFormFileStub.Type
    }

    override fun getWhitespaceTokens(): TokenSet {
        return FortranTokenSets.WHITE_SPACES
    }

    override fun getCommentTokens(): TokenSet {
        return FortranTokenSets.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return FortranTokenSets.STRINGS
    }

    override fun createElement(astNode: ASTNode): PsiElement {
        val element = FortranManualPsiElementFactory.createElement(astNode)
        return element ?: FortranTypes.Factory.createElement(astNode)
    }

    override fun createFile(fileViewProvider: FileViewProvider): PsiFile {
        return FortranFixedFormFile(fileViewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(astNode: ASTNode, astNode1: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }
}