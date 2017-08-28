package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import com.intellij.psi.stubs.IndexSink
import org.jetbrains.fortran.lang.core.stubs.index.FortranNamedElementIndex
import org.jetbrains.fortran.lang.psi.FortranEntityDecl

class FortranEntityDeclStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>,
        override val name: String?
): StubBase<FortranEntityDecl>(parent, elementType), FortranNamedStub {
    class Type<PsiT : FortranEntityDecl>(
            debugName: String,
            private val psiCtor: (FortranEntityDeclStub, IStubElementType<*, *>) -> PsiT
    ) : FortranStubElementType<FortranEntityDeclStub, FortranEntityDecl>(debugName) {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranEntityDeclStub =
                FortranEntityDeclStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: FortranEntityDeclStub, dataStream: StubOutputStream) {
            with(dataStream) {
                writeName(stub.name)
            }
        }

        override fun createPsi(stub: FortranEntityDeclStub) = psiCtor(stub, this)

        override fun createStub(psi: FortranEntityDecl, parentStub: StubElement<*>?) =
                FortranEntityDeclStub(parentStub, this, psi.name)

        override fun indexStub(stub: FortranEntityDeclStub, sink: IndexSink) {
            stub.name?.let {
                sink.occurrence(FortranNamedElementIndex.KEY, it)
            }
        }
    }
}