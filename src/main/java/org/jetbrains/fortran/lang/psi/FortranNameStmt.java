package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement;

public interface FortranNameStmt extends FortranCompositeElement, FortranNamedElement {
    @Nullable
    FortranEntityDecl getEntityDecl();
}
