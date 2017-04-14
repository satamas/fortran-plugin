package org.jetbrains.fortran.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import org.jetbrains.fortran.lang.psi.*;

public class FortranLabeledDoConstructImpl extends FortranExprImpl implements FortranLabeledDoConstruct {

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

}
