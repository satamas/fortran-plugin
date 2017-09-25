package org.jetbrains.fortran.lang.psi;

import org.jetbrains.annotations.Nullable;


public interface FortranBeginUnitStmt extends FortranStmt {
    @Nullable
    FortranEntityDecl getEntityDecl();
}
