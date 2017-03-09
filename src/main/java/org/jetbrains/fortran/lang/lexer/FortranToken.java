package org.jetbrains.fortran.lang.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.FortranLanguage;

public class FortranToken extends IElementType {
    public FortranToken(@NotNull @NonNls String debugName) {
        super(debugName, FortranLanguage.INSTANCE);
    }
}
