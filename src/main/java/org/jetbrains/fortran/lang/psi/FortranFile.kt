package org.jetbrains.fortran.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.FileViewProvider
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.stubs.FortranProgramUnitStub

class FortranFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, FortranLanguage), FortranCompositeElement {

    override fun getFileType(): FortranFileType = FortranFileType

    val programUnits : List<FortranProgramUnit>
        get() = runReadAction {
            if (stub != null)
                stub!!.childrenStubs
                        .filter { it is FortranProgramUnitStub }
                        .map { it.psi as FortranProgramUnit }
            else
                children.filter { it is FortranProgramUnit }.map { it as FortranProgramUnit }
        }
}
