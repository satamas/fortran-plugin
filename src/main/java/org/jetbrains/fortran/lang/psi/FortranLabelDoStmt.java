package org.jetbrains.fortran.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FortranLabelDoStmt extends FortranStmt {

    @NotNull
    FortranLabel getNumericalLabel();

    @Nullable
    FortranLabelDecl getNumericalLabelDecl();

    @Nullable
    FortranExpr getExpr();

    @Nullable
    PsiElement getColon();

    @Nullable
    PsiElement getEol();
}