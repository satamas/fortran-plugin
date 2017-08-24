package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranProgramUnit

class FortranProgramUnitStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>,
        override val name: String?
): StubBase<FortranProgramUnit>(parent, elementType), FortranNamedStub {
    class Type<PsiT : FortranProgramUnit>(
            debugName: String,
            private val psiCtor: (FortranProgramUnitStub, IStubElementType<*, *>) -> PsiT
    ) : FortranStubElementType<FortranProgramUnitStub, FortranProgramUnit>(debugName) {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranProgramUnitStub =
                FortranProgramUnitStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: FortranProgramUnitStub, dataStream: StubOutputStream) {
            with(dataStream) {
                writeName(stub.name)
            }
        }

        override fun createPsi(stub: FortranProgramUnitStub) = psiCtor(stub, this)

        override fun createStub(psi: FortranProgramUnit, parentStub: StubElement<*>?) =
                FortranProgramUnitStub(parentStub, this, psi.name)

        override fun indexStub(stub: FortranProgramUnitStub, sink: IndexSink) {
            // NOP
        }
    }
}