package org.jetbrains.fortran.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.FortranFixedFormFileType;
import org.jetbrains.fortran.FortranFixedFormLanguage;

public class FortranFixedFormFile extends PsiFileBase{
    public FortranFixedFormFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, FortranFixedFormLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FortranFixedFormFileType.INSTANCE;
    }
}
