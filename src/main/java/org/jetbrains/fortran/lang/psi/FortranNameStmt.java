package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface FortranNameStmt extends FortranCompositeElement {
    @Nullable
    PsiElement getIdentifier();
}
