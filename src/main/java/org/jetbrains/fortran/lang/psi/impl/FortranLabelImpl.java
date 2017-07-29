package org.jetbrains.fortran.lang.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import static java.lang.Integer.parseInt;
import static org.jetbrains.fortran.lang.FortranTypes.*;
import org.jetbrains.fortran.lang.psi.*;

public class FortranLabelImpl extends FortranCompositeElementImpl implements FortranLabel {

    public FortranLabelImpl(ASTNode node) {
        super(node);
    }

    @Override
    @NotNull
    public PsiElement getIntegerliteral() {
        return findNotNullChildByType(INTEGERLITERAL);
    }

    @Override
    public int gelLabelValue() {
        return parseInt(getText());
    }

}