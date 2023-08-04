package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranInternalSubprogramPart
import org.jetbrains.fortran.lang.psi.impl.FortranInternalSubprogramPartImpl

class FortranInternalSubprogramPartStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranInternalSubprogramPart>(parent, elementType){

    object Type :
        FortranStubElementType<FortranInternalSubprogramPartStub, FortranInternalSubprogramPart>("INTERNAL_SUBPROGRAM_PART") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranInternalSubprogramPartStub {
            return FortranInternalSubprogramPartStub(parentStub, this)
        }

        override fun serialize(stub: FortranInternalSubprogramPartStub, dataStream: StubOutputStream) {
        }

        override fun createPsi(stub: FortranInternalSubprogramPartStub) = FortranInternalSubprogramPartImpl(stub, this)

        override fun createStub(psi: FortranInternalSubprogramPart, parentStub: StubElement<*>?) =
                FortranInternalSubprogramPartStub(parentStub, this)

        override fun indexStub(stub: FortranInternalSubprogramPartStub, sink: IndexSink) {
            // NOP
        }
    }
}