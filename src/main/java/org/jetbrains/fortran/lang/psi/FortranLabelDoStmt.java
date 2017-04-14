package org.jetbrains.fortran.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FortranLabelDoStmt extends FortranExpr {

    @Nullable
    FortranExpr getExpr();

    @Nullable
    PsiElement getColon();

    @NotNull
    PsiElement getDo();

    @Nullable
    PsiElement getEol();

    @Nullable
    PsiElement getIdentifier();

    @Nullable
    PsiElement getLabel();

}