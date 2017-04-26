package org.jetbrains.fortran;

import com.intellij.lang.Language;

public class FortranFixedFormLanguage extends Language {
    public static final FortranFixedFormLanguage INSTANCE = new FortranFixedFormLanguage();

    protected FortranFixedFormLanguage() {
        super("Fortran fixed form", "text/fortran");
    }
}
