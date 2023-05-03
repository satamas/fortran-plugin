package org.jetbrains.fortran.lang.psi

import com.intellij.psi.PsiElement

interface FortranLabelDoStmt : FortranBeginConstructStmt {
    override val constructNameDecl: FortranConstructNameDecl?
    val label: FortranLabel
    val labelDecl: FortranLabelDecl?
    val expr: FortranExpr?
    val colon: PsiElement?
    val eol: PsiElement?
}