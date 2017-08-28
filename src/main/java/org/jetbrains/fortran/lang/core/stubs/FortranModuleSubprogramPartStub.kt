package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranModuleSubprogramPart
import org.jetbrains.fortran.lang.psi.impl.FortranModuleSubprogramPartImpl


class FortranModuleSubprogramPartStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranModuleSubprogramPart>(parent, elementType){

    object Type: FortranStubElementType<FortranModuleSubprogramPartStub, FortranModuleSubprogramPart>("MODULE SUBPROGRAM") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranModuleSubprogramPartStub {
            return FortranModuleSubprogramPartStub(parentStub, this)
        }

        override fun serialize(stub: FortranModuleSubprogramPartStub, dataStream: StubOutputStream) {
        }

        override fun createPsi(stub: FortranModuleSubprogramPartStub) = FortranModuleSubprogramPartImpl(stub, this)

        override fun createStub(psi: FortranModuleSubprogramPart, parentStub: StubElement<*>?) =
                FortranModuleSubprogramPartStub(parentStub, this)

        override fun indexStub(stub: FortranModuleSubprogramPartStub, sink: IndexSink) {
            // NOP
        }
    }
}