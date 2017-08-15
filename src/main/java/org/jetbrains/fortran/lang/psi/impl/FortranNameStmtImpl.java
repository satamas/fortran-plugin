package org.jetbrains.fortran.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.fortran.lang.FortranTypes;
import org.jetbrains.fortran.lang.psi.FortranEntityDecl;
import org.jetbrains.fortran.lang.psi.FortranNameStmt;
import org.jetbrains.fortran.lang.psi.FortranStmt;
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl;

public class FortranNameStmtImpl extends FortranNamedElementImpl implements FortranNameStmt {
    public FortranNameStmtImpl(ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NotNull String name) {
        return this;
    }

    @Nullable
    @Override
    public FortranEntityDecl getEntityDecl() {
        return findChildByType(FortranTypes.ENTITY_DECL);
    }
}
