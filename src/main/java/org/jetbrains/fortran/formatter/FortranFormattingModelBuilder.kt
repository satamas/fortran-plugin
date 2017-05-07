package org.jetbrains.fortran.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes.*

class FortranFormattingModelBuilder : FormattingModelBuilder {

    fun createSpacingBuilder(settings : CodeStyleSettings) : SpacingBuilder
            = SpacingBuilder(settings, FortranLanguage.INSTANCE)
            .before(COMMA).spaceIf(false)
            .after(COMMA).spaceIf(true)
            .after(IF).spaces(1);
    
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {

        val block = FortranFmtBlock(element.node, null, Indent.getNoneIndent(), null, settings, createSpacingBuilder(settings))

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
