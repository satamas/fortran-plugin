package org.jetbrains.fortran.lang.psi;

import org.jetbrains.annotations.Nullable;


public interface FortranEndUnitStmt extends FortranStmt {
    @Nullable
    default FortranDataPath getDataPath() {
        return null;
    }
}
