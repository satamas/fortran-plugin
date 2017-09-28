package org.jetbrains.fortran.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.FileViewProvider
import org.jetbrains.fortran.FortranFixedFormFileType
import org.jetbrains.fortran.FortranFixedFormLanguage
import org.jetbrains.fortran.lang.stubs.FortranProgramUnitStub


class FortranFixedFormFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, FortranFixedFormLanguage), FortranCompositeElement {

    override fun getFileType(): FortranFixedFormFileType = FortranFixedFormFileType

    val programUnits : List<FortranProgramUnit>
        get() = runReadAction {
            if (stub != null)
                stub!!.childrenStubs.filter { it is FortranProgramUnitStub }.map { it.psi as FortranProgramUnit }
            else
                children.filter { it is FortranProgramUnit }.map { it as FortranProgramUnit }
        }
}