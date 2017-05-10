package org.jetbrains.fortran;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FortranFileType extends LanguageFileType {
    public static FortranFileType INSTANCE = new FortranFileType();
    @NonNls
    public static final String[] DEFAULT_EXTENSIONS = {"f90", "f95", "f03", "f08"}; // f15 is not ready

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
