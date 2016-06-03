package org.jetbrains.fortran.lang.lexer;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FortranKeywordToken extends FortranSingleValueToken {
    /**
     * Generate keyword (identifier that has a keyword meaning in all possible contexts)
     */
    public static FortranKeywordToken keyword(String value) {
        return keyword(value, value);
    }

    public static FortranKeywordToken keyword(String debugName, String value) {
        return new FortranKeywordToken(debugName, value);
    }

    protected FortranKeywordToken(@NotNull @NonNls String debugName, @NotNull @NonNls String value) {
        super(debugName, value);
    }
}
