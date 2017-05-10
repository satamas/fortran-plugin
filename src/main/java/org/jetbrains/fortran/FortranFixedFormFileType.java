package org.jetbrains.fortran;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FortranFixedFormFileType extends LanguageFileType {
    public static FortranFixedFormFileType INSTANCE = new FortranFixedFormFileType();
    @NonNls
    public static final String[] DEFAULT_EXTENSIONS = {"f", "for", "F", "FOR"};

    protected FortranFixedFormFileType() {
        super(FortranFixedFormLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Fortran fixed form";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Fortran language file with fixed source form";
    }

    @NonNls
    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSIONS[0];
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return FortranIcons.INSTANCE.getFileTypeIcon();
    }
}
