package org.jetbrains.fortran.lang.lexer;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FortranSingleValueToken extends FortranToken {
    private final String value;

    public FortranSingleValueToken(@NotNull @NonNls String debugName, @NotNull @NonNls String value) {
        super(debugName);
        this.value = value;
    }

    @NotNull
    @NonNls
    public String getValue() {
        return value;
    }
}
