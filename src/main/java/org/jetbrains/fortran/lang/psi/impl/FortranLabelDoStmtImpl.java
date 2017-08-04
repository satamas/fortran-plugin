package org.jetbrains.fortran.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.fortran.lang.psi.FortranExpr;
import org.jetbrains.fortran.lang.psi.FortranNumericalLabel;
import org.jetbrains.fortran.lang.psi.FortranNumericalLabelDecl;
import org.jetbrains.fortran.lang.psi.FortranLabelDoStmt;

import static org.jetbrains.fortran.lang.FortranTypes.COLON;
import static org.jetbrains.fortran.lang.FortranTypes.EOL;

public class FortranLabelDoStmtImpl extends FortranStmtImpl implements FortranLabelDoStmt {

    public FortranLabelDoStmtImpl(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public FortranNumericalLabel getNumericalLabel() { return findNotNullChildByClass(FortranNumericalLabel.class); }

    @Override
    @Nullable
    public FortranNumericalLabelDecl getNumericalLabelDecl() { return findNotNullChildByClass(FortranNumericalLabelDecl.class); }

    @Override
    @Nullable
    public FortranExpr getExpr() {
        return findChildByClass(FortranExpr.class);
    }

    @Override
    @Nullable
    public PsiElement getColon() {
        return findChildByType(COLON);
    }


    @Override
    @Nullable
    public PsiElement getEol() {
        return findChildByType(EOL);
    }

}