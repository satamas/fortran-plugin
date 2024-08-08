package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import org.jetbrains.fortran.FortranFixedFormLanguage
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile

class FortranFixedFormFileStub(file: FortranFixedFormFile?) : PsiFileStubImpl<FortranFixedFormFile>(file) {

    override fun getType() = Type

    object Type : IStubFileElementType<FortranFixedFormFileStub>(FortranFixedFormLanguage) {
        override fun getStubVersion(): Int = 4

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> = FortranFixedFormFileStub(file as FortranFixedFormFile)
        }

        override fun serialize(stub: FortranFixedFormFileStub, dataStream: StubOutputStream) {
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranFixedFormFileStub {
            return FortranFixedFormFileStub(null)
        }

        override fun getExternalId(): String = "Fortran_fixed.file"
    }
}