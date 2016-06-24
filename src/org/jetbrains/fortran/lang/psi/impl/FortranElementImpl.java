package org.jetbrains.fortran.lang.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.lang.psi.FortranElement;
import org.jetbrains.fortran.lang.psi.FortranVisitor;

public class FortranElementImpl extends ASTWrapperPsiElement implements FortranElement {

    public FortranElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FortranVisitor) {
            accept((FortranVisitor) visitor);
        } else {
            visitor.visitElement(this);
        }
    }

    @Override
    public void accept(FortranVisitor visitor) {
        visitor.visitFormElement(this);
    }

//    public String toString() {
//        return "";
//    }
}
