package org.jetbrains.fortran.lang.psi;

import java.util.List;

import org.jetbrains.annotations.*;

public interface FortranLabeledDoConstruct extends FortranExecutableConstruct {

    @NotNull
    List<FortranExpr> getExprList();

    @NotNull
    FortranLabelDoStmt getLabelDoStmt ();

    @Nullable
    FortranStmt getDoTermActionStmt();

    @Nullable
    FortranLabeledDoConstruct getLabeledDoTermConstract();

    @Nullable
    FortranEndDoStmt getEndDoStmt();
}
