package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranBlock
import org.jetbrains.fortran.lang.psi.impl.FortranBlockImpl


class FortranBlockStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranBlock>(parent, elementType){

    object Type: FortranStubElementType<FortranBlockStub, FortranBlock>("BLOCK") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranBlockStub {
            return FortranBlockStub(parentStub, this)
        }

        override fun serialize(stub: FortranBlockStub, dataStream: StubOutputStream) {
        }

        override fun createPsi(stub: FortranBlockStub) = FortranBlockImpl(stub, this)

        override fun createStub(psi: FortranBlock, parentStub: StubElement<*>?) =
                FortranBlockStub(parentStub, this)

        override fun indexStub(stub: FortranBlockStub, sink: IndexSink) {
            // NOP
        }
    }
}