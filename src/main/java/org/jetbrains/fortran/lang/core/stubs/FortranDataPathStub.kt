package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import com.intellij.psi.stubs.IndexSink
import org.jetbrains.fortran.lang.core.stubs.index.FortranNamedElementIndex
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.impl.FortranDataPathImpl

class FortranDataPathStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>,
        override val name: String?
): StubBase<FortranDataPath>(parent, elementType), FortranNamedStub {

    class Type<PsiT : FortranDataPath>(
            debugName: String,
            private val psiCtor: (FortranDataPathStub, IStubElementType<*, *>) -> PsiT
    ) : FortranStubElementType<FortranDataPathStub, FortranDataPath>(debugName) {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranDataPathStub =
                FortranDataPathStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: FortranDataPathStub, dataStream: StubOutputStream) {
            with(dataStream) {
                writeName(stub.name)
            }
        }

        override fun createPsi(stub: FortranDataPathStub) = psiCtor(stub, this)

        override fun createStub(psi: FortranDataPath, parentStub: StubElement<*>?) =
                FortranDataPathStub(parentStub, this, psi.referenceName)

        override fun indexStub(stub: FortranDataPathStub, sink: IndexSink) {
            stub.name?.let {
                sink.occurrence(FortranNamedElementIndex.KEY, it)
            }
        }
    }
}