package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement;

public interface FortranNameStmt extends FortranStmt {
    @Nullable
    FortranEntityDecl getEntityDecl();
}
