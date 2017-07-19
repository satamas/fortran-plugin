package org.jetbrains.fortran.lang.psi.impl;

import java.util.List;
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
    public FortranDoTermActionStmt getDoTermActionStmt() {
        return findChildByClass(FortranDoTermActionStmt.class);
    }

}
