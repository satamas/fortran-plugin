package org.jetbrains.fortran.lang.lexer;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FortranKeywordToken extends FortranSingleValueToken {
    private boolean isSoft;
    /**
     * Generate keyword (identifier that has a keyword meaning in all possible contexts)
     */
    public static FortranKeywordToken keyword(String value) {
        return keyword(value, value);
    }

    public static FortranKeywordToken softKeyword(String value) {
        return softKeyword(value, value);
    }

    public static FortranKeywordToken keyword(String debugName, String value) {
        return new FortranKeywordToken(debugName, value, false);
    }

    public static FortranKeywordToken softKeyword(String debugName, String value) {
        return new FortranKeywordToken(debugName, value, true);
    }

    protected FortranKeywordToken(@NotNull @NonNls String debugName, @NotNull @NonNls String value, boolean isSoft) {
        super(debugName, value);
        this.isSoft = isSoft;
    }

    public boolean isSoft() {
        return isSoft;
    }
}
