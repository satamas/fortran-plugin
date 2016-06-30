package org.jetbrains.fortran.lang.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class FortranCallOrAccessExpression extends FortranElementImpl {
    public FortranCallOrAccessExpression(@NotNull ASTNode node) {
        super(node);
    }
}
