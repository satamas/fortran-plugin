package org.jetbrains.fortran.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.FortranTokenType.*

abstract class FortranParserDefinitionBase : ParserDefinition {
    override fun createParser(project: Project) : PsiParser = FortranParser()

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(astNode: ASTNode): PsiElement =
            FortranManualPsiElementFactory.createElement(astNode)
            ?: FortranTypes.Factory.createElement(astNode)

    override fun spaceExistanceTypeBetweenTokens(astNode: ASTNode, astNode1: ASTNode): ParserDefinition.SpaceRequirements =
            ParserDefinition.SpaceRequirements.MAY
}