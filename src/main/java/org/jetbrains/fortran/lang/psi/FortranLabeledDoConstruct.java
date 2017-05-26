package org.jetbrains.fortran.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FortranLabeledDoConstruct extends FortranExecutableConstruct {

    @NotNull
    List<FortranExpr> getExprList();

}
