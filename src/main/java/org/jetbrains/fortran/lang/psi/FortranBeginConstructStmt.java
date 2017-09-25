package org.jetbrains.fortran.lang.psi;

import org.jetbrains.annotations.Nullable;

public interface FortranBeginConstructStmt extends FortranStmt {
    @Nullable
    FortranConstructNameDecl getConstructNameDecl();
}
