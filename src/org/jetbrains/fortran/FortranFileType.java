package org.jetbrains.fortran;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FortranFileType extends LanguageFileType {
    public static FortranFileType INSTANCE = new FortranFileType();
    public static final String DEFAULT_EXTENSION = "f";

    protected FortranFileType() {
        super(FortranLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Fortran";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Fortran language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }
}
