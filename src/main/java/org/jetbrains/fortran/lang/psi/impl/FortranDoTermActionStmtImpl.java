package org.jetbrains.fortran.lang.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.fortran.lang.psi.*;

public class FortranDoTermActionStmtImpl extends FortranExprImpl implements FortranDoTermActionStmt {

    public FortranDoTermActionStmtImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FortranVisitor) accept((FortranVisitor)visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getExpr() {
        return findChildByClass(FortranExpr.class);
    }
}
