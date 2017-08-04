package org.jetbrains.fortran.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FortranLabelDoStmt extends FortranStmt {

    @NotNull
    FortranNumericalLabel getNumericalLabel();

    @Nullable
    FortranNumericalLabelDecl getNumericalLabelDecl();

    @Nullable
    FortranExpr getExpr();

    @Nullable
    PsiElement getColon();

    @Nullable
    PsiElement getEol();
}