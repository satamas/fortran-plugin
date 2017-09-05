package org.jetbrains.fortran.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.stubs.FortranProgramUnitStub

class FortranFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, FortranLanguage) {

    override fun getFileType(): FortranFileType = FortranFileType

    val programUnits : Array<FortranProgramUnit>
        get() = if (stub != null)
            stub!!.childrenStubs.filter { it is FortranProgramUnitStub }.map{ it.psi as FortranProgramUnit}.toTypedArray()
        else
            children.filter { it is FortranProgramUnit }.map{ it as FortranProgramUnit}.toTypedArray()

}
