package org.jetbrains.fortran.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.FortranFileType;
import org.jetbrains.fortran.FortranLanguage;

public class FortranFile extends PsiFileBase{
    public FortranFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, FortranLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return FortranFileType.INSTANCE;
    }
}
