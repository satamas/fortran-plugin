package org.jetbrains.fortran.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranLanguage

class FortranFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, FortranLanguage) {

    override fun getFileType(): FortranFileType = FortranFileType

}
