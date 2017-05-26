package org.jetbrains.fortran.lang.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.fortran.lang.psi.FortranStmt;

public class FortranStmtImpl extends FortranCompositeElementImpl implements FortranStmt {
    public FortranStmtImpl(ASTNode node) {
        super(node);
    }
}
