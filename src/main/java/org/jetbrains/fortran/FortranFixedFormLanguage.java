package org.jetbrains.fortran;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class FortranFixedFormLanguage extends Language {
    public static final FortranFixedFormLanguage INSTANCE = new FortranFixedFormLanguage();
    public static final String NAME = "Fortran fixed form";

    protected FortranFixedFormLanguage() {
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
