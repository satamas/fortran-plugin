package org.jetbrains.fortran.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORD

class FortranFormattingModelBuilder : FormattingModelBuilder {

    fun createSpacingBuilder(settings : CodeStyleSettings) : SpacingBuilder
            = SpacingBuilder(settings, FortranLanguage.INSTANCE)
            .after(KEYWORD).spaces(1)
            .before(COMMA).spaceIf(false)
            .after(COMMA).spaceIf(true)
            .around(LT).spaces(1)
            .around(LE).spaces(1)
            .around(GT).spaces(1)
            .around(GE).spaces(1)
            .around(EQ).spaces(1)
            .around(POINTER_ASSMNT).spaces(1)
            .around(EQEQ).spaces(1)
            .around(NEQ).spaces(1)
            .around(LOGICAL_EQ).spaces(1)
            .around(LOGICAL_NEQ).spaces(1)
            .around(AND).spaces(1)
            .around(OR).spaces(1)
            .around(NOT).spaces(1)
            .aroundInside(PLUS, ADD_EXPR).spaces(1)
            .before(PLUS).spaces(1)
            .after(PLUS).none()
            .aroundInside(MINUS, ADD_EXPR).spaces(1)
            .before(MINUS).spaces(1)
            .after(MINUS).none()
            .aroundInside(MUL, EXPR).spaces(1)
            .around(MUL).none()
            .around(DIV).spaces(1)
            .around(DIVDIV).spaces(1)


    
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {

        val block = FortranFmtBlock(element.node, null, Indent.getNormalIndent(), null, settings, createSpacingBuilder(settings))

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
