package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.PsiElement;

public interface FortranElement extends PsiElement {
    void accept(FortranVisitor visitor);
}
