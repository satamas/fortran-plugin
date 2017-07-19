package org.jetbrains.fortran.lang.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.fortran.lang.psi.*;

public class FortranDoTermActionStmtImpl extends FortranStmtImpl implements FortranDoTermActionStmt {

    public FortranDoTermActionStmtImpl(ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public PsiElement getExpr() {
        return findChildByClass(FortranExpr.class);
    }
}
