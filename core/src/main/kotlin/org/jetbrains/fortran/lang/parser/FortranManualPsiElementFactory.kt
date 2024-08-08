package org.jetbrains.fortran.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDeclImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelDoStmtImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabelImpl
import org.jetbrains.fortran.lang.psi.impl.FortranLabeledDoConstructImpl

object FortranManualPsiElementFactory {
    @JvmStatic
    fun createElement(node: ASTNode): PsiElement? {
        val type = node.elementType
        return when {
            type === FortranTypes.LABEL                 -> FortranLabelImpl(node)
            type === FortranTypes.LABEL_DECL            -> FortranLabelDeclImpl(node)
            type === FortranTypes.LABELED_DO_CONSTRUCT  -> FortranLabeledDoConstructImpl(node)
            type === FortranTypes.LABEL_DO_STMT         -> FortranLabelDoStmtImpl(node)
            else -> null
        }
    }
}
