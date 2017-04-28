package org.jetbrains.fortran.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.FortranTypes

class FortranFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement?, settings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(element?.containingFile,
                FortranBlock(element!!.node,
                        Wrap.createWrap(WrapType.NONE,false),
                        Alignment.createAlignment(),
                        createSpaceBuilder(settings)),
                        settings)
    }

    fun createSpaceBuilder(settings: CodeStyleSettings) : SpacingBuilder {
        return SpacingBuilder(settings, FortranLanguage.INSTANCE).
             around(FortranTypes.EQ)
                .spaceIf(settings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .before(FortranTypes.EQ)
                .none()
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
