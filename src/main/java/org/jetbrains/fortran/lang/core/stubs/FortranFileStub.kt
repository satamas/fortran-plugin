package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.psi.FortranFile

class FortranFileStub(file: FortranFile?) : PsiFileStubImpl<FortranFile>(file) {
    override fun getType() = Type

    object Type : IStubFileElementType<FortranFileStub>(FortranLanguage) {
        override fun getStubVersion(): Int = 1

        override fun getBuilder(): StubBuilder = object : DefaultStubBuilder() {
            override fun createStubForFile(file: PsiFile): StubElement<*> = FortranFileStub(file as FortranFile)
        }

        override fun serialize(stub: FortranFileStub, dataStream: StubOutputStream) {
        }

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranFileStub {
            return FortranFileStub(null)
        }

        override fun getExternalId(): String = "Fortran.file"
    }
}