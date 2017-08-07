package org.jetbrains.fortran.ide.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.ide.formatter.settings.FortranCodeStyleSettings
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType.KEYWORD

class FortranFormattingModelBuilder : FormattingModelBuilder {

    fun createSpacingBuilder(settings : CodeStyleSettings) : SpacingBuilder {
        val fortranCommonSettings = settings.getCommonSettings(FortranLanguage)
        val fortranSettings = settings.getCustomSettings(FortranCodeStyleSettings::class.java)

        return SpacingBuilder(settings, FortranLanguage)
                .around(EQ).spaceIf(fortranCommonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(POINTER_ASSMNT).spaceIf(fortranCommonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(AND).spaceIf(fortranCommonSettings.SPACE_AROUND_LOGICAL_OPERATORS)
                .around(OR).spaceIf(fortranCommonSettings.SPACE_AROUND_LOGICAL_OPERATORS)
                .around(EQEQ).spaceIf(fortranCommonSettings.SPACE_AROUND_EQUALITY_OPERATORS)
                .around(NEQ).spaceIf(fortranCommonSettings.SPACE_AROUND_EQUALITY_OPERATORS)
                .around(LT).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .around(LE).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .around(GT).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .around(GE).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .aroundInside(PLUS, ADD_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
                .aroundInside(MINUS, ADD_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
                .aroundInside(MUL, MULT_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
                .aroundInside(DIV, MULT_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
                .before(PLUS).spaces(1)
                .after(PLUS).spaceIf(fortranCommonSettings.SPACE_AROUND_UNARY_OPERATOR)
                .before(MINUS).spaces(1)
                .after(MINUS).spaceIf(fortranCommonSettings.SPACE_AROUND_UNARY_OPERATOR)
                .around(NOT).spaceIf(fortranSettings.SPACE_AROUND_NOT_OPERATOR)
                .around(POWER).spaceIf(fortranSettings.SPACE_AROUND_POWER_OPERATOR)
                .around(LOGICAL_EQ).spaceIf(fortranSettings.SPACE_AROUND_EQUIVALENCE_OPERATOR)
                .around(LOGICAL_NEQ).spaceIf(fortranSettings.SPACE_AROUND_EQUIVALENCE_OPERATOR)
                .aroundInside(DIVDIV, CONCAT_EXPR).spaceIf(fortranSettings.SPACE_AROUND_CONCAT_OPERATOR)
                .aroundInside(DEFOPERATOR, DEF_BINARY_OPERATOR_EXPR).spaceIf(fortranSettings.SPACE_AROUND_DEFINED_OPERATOR)
                .aroundInside(DEFOPERATOR, DEF_UNARY_OPERATOR_EXPR).spaceIf(fortranSettings.SPACE_AROUND_DEFINED_OPERATOR)
                .between(KEYWORD, KEYWORD).spaces(1)
                .between(KEYWORD, IDENTIFIER).spaces(1)
                .between(IDENTIFIER, KEYWORD).spaces(1)
                .before(COMMA).spaceIf(fortranCommonSettings.SPACE_BEFORE_COMMA)
                .after(COMMA).spaceIf(fortranCommonSettings.SPACE_AFTER_COMMA)
                .before(COLON).spaceIf(fortranCommonSettings.SPACE_BEFORE_COLON)
                .after(COLON).spaceIf(fortranCommonSettings.SPACE_AFTER_COLON)
                .after(CONSTRUCT_NAME_DECL).spaceIf(fortranCommonSettings.SPACE_AFTER_COLON)
                .before(COLONCOLON).spaceIf(fortranSettings.SPACE_BEFORE_DOUBLE_COLON)
                .after(COLONCOLON).spaceIf(fortranSettings.SPACE_AFTER_DOUBLE_COLON)
                .after(LPAR).spaces(0)
                .before(RPAR).spaces(0)

    }

    
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {

        val block = FortranFmtBlock(element.node, null, Indent.getNormalIndent(), null, settings, createSpacingBuilder(settings))

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
