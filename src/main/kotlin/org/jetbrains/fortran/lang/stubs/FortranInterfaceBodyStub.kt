package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.stubs.index.FortranNamedElementIndex
import org.jetbrains.fortran.lang.psi.FortranInterfaceBody
import org.jetbrains.fortran.lang.psi.impl.FortranInterfaceBodyImpl
import org.jetbrains.fortran.lang.psi.mixin.FortranInterfaceBodyImplMixin

class FortranInterfaceBodyStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>,
        override val name: String?
): StubBase<FortranInterfaceBody>(parent, elementType), FortranNamedStub {
    object Type : FortranStubElementType<FortranInterfaceBodyStub, FortranInterfaceBody>("Interface body") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranInterfaceBodyStub =
                FortranInterfaceBodyStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: FortranInterfaceBodyStub, dataStream: StubOutputStream) {
            with(dataStream) {
                writeName(stub.name)
            }
        }

        override fun createPsi(stub: FortranInterfaceBodyStub) = FortranInterfaceBodyImpl(stub, this)

        override fun createStub(psi: FortranInterfaceBody, parentStub: StubElement<*>?) =
                FortranInterfaceBodyStub(parentStub, this, (psi as FortranInterfaceBodyImplMixin).name)

        override fun indexStub(stub: FortranInterfaceBodyStub, sink: IndexSink) {
            stub.name?.let {
                sink.occurrence(FortranNamedElementIndex.KEY, it)
            }
        }
    }
}