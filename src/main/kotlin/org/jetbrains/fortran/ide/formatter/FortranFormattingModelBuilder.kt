package org.jetbrains.fortran.ide.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.ide.formatter.settings.FortranCodeStyleSettings
import org.jetbrains.fortran.lang.FortranTypes.*
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranFormattingModelBuilder : FormattingModelBuilder {

    fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val fortranCommonSettings = settings.getCommonSettings(FortranLanguage)
        val fortranSettings = settings.getCustomSettings(FortranCodeStyleSettings::class.java)

        return SpacingBuilder(settings, FortranLanguage)
                .aroundInside(EQ, GENERIC_SPEC).spaces(0)
                .around(EQ).spaceIf(fortranCommonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .around(POINTER_ASSMNT).spaceIf(fortranCommonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                .aroundInside(AND, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_LOGICAL_OPERATORS)
                .aroundInside(OR, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_LOGICAL_OPERATORS)
                .aroundInside(EQEQ, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_EQUALITY_OPERATORS)
                .aroundInside(NEQ, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_EQUALITY_OPERATORS)
                .aroundInside(LT, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .aroundInside(LE, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .aroundInside(GT, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .aroundInside(GE, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
                .aroundInside(PLUS, ADD_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
                .aroundInside(MINUS, ADD_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
                .aroundInside(MUL, MULT_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
                .aroundInside(DIV, MULT_EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
                .beforeInside(PLUS, EXPR).spaces(1)
                .afterInside(PLUS, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_UNARY_OPERATOR)
                .beforeInside(MINUS, EXPR).spaces(1)
                .afterInside(MINUS, EXPR).spaceIf(fortranCommonSettings.SPACE_AROUND_UNARY_OPERATOR)
                .aroundInside(NOT, EXPR).spaceIf(fortranSettings.SPACE_AROUND_NOT_OPERATOR)
                .aroundInside(POWER, EXPR).spaceIf(fortranSettings.SPACE_AROUND_POWER_OPERATOR)
                .aroundInside(LOGICAL_EQ, EXPR).spaceIf(fortranSettings.SPACE_AROUND_EQUIVALENCE_OPERATOR)
                .aroundInside(LOGICAL_NEQ, EXPR).spaceIf(fortranSettings.SPACE_AROUND_EQUIVALENCE_OPERATOR)
                .aroundInside(DIVDIV, CONCAT_EXPR).spaceIf(fortranSettings.SPACE_AROUND_CONCAT_OPERATOR)
                .aroundInside(DEFOPERATOR, DEF_BINARY_OPERATOR_EXPR).spaceIf(fortranSettings.SPACE_AROUND_DEFINED_OPERATOR)
                .aroundInside(DEFOPERATOR, DEF_UNARY_OPERATOR_EXPR).spaceIf(fortranSettings.SPACE_AROUND_DEFINED_OPERATOR)
                .between(FortranTokenType.KEYWORDS, FortranTokenType.KEYWORDS).spaces(1)
                .between(FortranTokenType.KEYWORDS, IDENTIFIER).spaces(1)
                .between(IDENTIFIER, FortranTokenType.KEYWORDS).spaces(1)
                .before(COMMA).spaceIf(fortranCommonSettings.SPACE_BEFORE_COMMA)
                .after(COMMA).spaceIf(fortranCommonSettings.SPACE_AFTER_COMMA)

                .beforeInside(COLON, CONSTRUCT_NAME_DECL).spaceIf(fortranCommonSettings.SPACE_BEFORE_COLON)
                .afterInside(COLON, CONSTRUCT_NAME_DECL).spaceIf(fortranCommonSettings.SPACE_AFTER_COLON)
                .beforeInside(COLON, USE_STMT).spaceIf(fortranCommonSettings.SPACE_BEFORE_COLON)
                .afterInside(COLON, USE_STMT).spaceIf(fortranCommonSettings.SPACE_AFTER_COLON)
                .beforeInside(COLON, PARENT_IDENTIFIER).spaceIf(fortranCommonSettings.SPACE_BEFORE_COLON)
                .afterInside(COLON, PARENT_IDENTIFIER).spaceIf(fortranCommonSettings.SPACE_AFTER_COLON)
                .around(COLON).spaces(0)

                .after(CONSTRUCT_NAME_DECL).spaceIf(fortranCommonSettings.SPACE_AFTER_COLON)
                .before(COLONCOLON).spaceIf(fortranSettings.SPACE_BEFORE_DOUBLE_COLON)
                .after(COLONCOLON).spaceIf(fortranSettings.SPACE_AFTER_DOUBLE_COLON)
                .after(LPAR).spaces(0)
                .before(RPAR).spaces(0)

                .before(MACRO).spacing(0, 0, 0, true, 1)

    }


    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {

        val block = FortranFmtBlock(element.node, null, Indent.getNormalIndent(), null, settings, createSpacingBuilder(settings), false)

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null
}
