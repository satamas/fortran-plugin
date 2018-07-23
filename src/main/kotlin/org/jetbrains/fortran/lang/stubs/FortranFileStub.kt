package org.jetbrains.fortran.lang.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.parser.FortranFileParser
import org.jetbrains.fortran.lang.psi.FortranFile

class FortranFileStub(file: FortranFile?) : PsiFileStubImpl<FortranFile>(file) {
    override fun getType() = Type

    object Type : IStubFileElementType<FortranFileStub>(FortranLanguage) {

        override fun parseContents(chameleon: ASTNode): ASTNode? {
            return FortranFileParser.parse(this, chameleon)
        }

        override fun getStubVersion(): Int = 2

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