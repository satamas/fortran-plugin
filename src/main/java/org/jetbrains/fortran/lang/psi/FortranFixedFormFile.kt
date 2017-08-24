package org.jetbrains.fortran.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.jetbrains.fortran.FortranFixedFormFileType
import org.jetbrains.fortran.FortranFixedFormLanguage
import org.jetbrains.fortran.lang.core.stubs.FortranFixedFormFileStub


class FortranFixedFormFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, FortranFixedFormLanguage) {

    override fun getFileType(): FortranFixedFormFileType = FortranFixedFormFileType

    override fun getStub(): FortranFixedFormFileStub? = super.getStub() as FortranFixedFormFileStub?
}