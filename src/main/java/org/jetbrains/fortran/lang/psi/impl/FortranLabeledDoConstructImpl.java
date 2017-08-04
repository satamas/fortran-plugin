package org.jetbrains.fortran.lang.psi.impl;

import java.util.List;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.fortran.lang.psi.*;

public class FortranLabeledDoConstructImpl extends FortranExecutableConstructImpl implements FortranLabeledDoConstruct {

    public FortranLabeledDoConstructImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FortranVisitor) accept((FortranVisitor)visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FortranExpr> getExprList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FortranExpr.class);
    }

    @NotNull
    @Override
    public FortranLabelDoStmt getLabelDoStmt() {
        return findNotNullChildByClass(FortranLabelDoStmt.class);
    }

    @Nullable
    @Override
    public FortranStmt getDoTermActionStmt() {
        return (getLastChild() instanceof FortranStmt) ? (FortranStmt)getLastChild() : null;
    }

    @Nullable
    @Override
    public FortranLabeledDoConstruct getLabeledDoTermConstract() {
        return (getLastChild() instanceof FortranLabeledDoConstruct) ? (FortranLabeledDoConstruct)getLastChild() : null;
    }

    @Nullable
    @Override
    public FortranEndDoStmt getEndDoStmt() {
        return findChildByClass(FortranEndDoStmt.class);
    }
}
