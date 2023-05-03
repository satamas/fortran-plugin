package org.jetbrains.fortran.lang.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.*

class FortranLabelDoStmtImpl(node: ASTNode?) : FortranStmtImpl(node), FortranLabelDoStmt {
    override val label: FortranLabel
        get() = findNotNullChildByClass(FortranLabel::class.java)
    override val labelDecl: FortranLabelDecl?
        get() = findNotNullChildByClass(FortranLabelDecl::class.java)
    override val constructNameDecl: FortranConstructNameDecl?
        get() = findChildByClass(FortranConstructNameDecl::class.java)
    override val expr: FortranExpr?
        get() = findChildByClass(FortranExpr::class.java)
    override val colon: PsiElement?
        get() = findChildByType(FortranTypes.COLON)
    override val eol: PsiElement?
        get() = findChildByType(FortranTypes.EOL)
}