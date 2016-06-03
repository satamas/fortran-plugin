package org.jetbrains.fortran;

import com.intellij.lang.Language;

public class FortranLanguage extends Language {
    public static final FortranLanguage INSTANCE = new FortranLanguage();

    protected FortranLanguage() {
        super("Fortran", "text/fortran");
    }
}
