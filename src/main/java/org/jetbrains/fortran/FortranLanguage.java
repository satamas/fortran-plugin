package org.jetbrains.fortran;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class FortranLanguage extends Language {
    public static final FortranLanguage INSTANCE = new FortranLanguage();
    public static final String NAME = "Fortran";

    protected FortranLanguage() {
        super(NAME, "text/fortran");
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return NAME;
    }

    @Override
    public boolean isCaseSensitive() {
        return false;
    }
}
