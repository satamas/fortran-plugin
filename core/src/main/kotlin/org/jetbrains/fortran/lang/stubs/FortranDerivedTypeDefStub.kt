package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranDerivedTypeDef
import org.jetbrains.fortran.lang.psi.impl.FortranDerivedTypeDefImpl
import org.jetbrains.fortran.lang.stubs.index.FortranNamedElementIndex

class FortranDerivedTypeDefStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>,
        override val name: String?
): StubBase<FortranDerivedTypeDef>(parent, elementType), FortranNamedStub {
    object Type : FortranStubElementType<FortranDerivedTypeDefStub, FortranDerivedTypeDef>("DERIVED_TYPE_DEF") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranDerivedTypeDefStub =
                FortranDerivedTypeDefStub(parentStub, this, dataStream.readName()?.string)

        override fun serialize(stub: FortranDerivedTypeDefStub, dataStream: StubOutputStream) {
            with(dataStream) {
                writeName(stub.name)
            }
        }

        override fun createPsi(stub: FortranDerivedTypeDefStub) = FortranDerivedTypeDefImpl(stub, this)

        override fun createStub(psi: FortranDerivedTypeDef, parentStub: StubElement<*>?) =
                FortranDerivedTypeDefStub(parentStub, this, psi.name)

        override fun indexStub(stub: FortranDerivedTypeDefStub, sink: IndexSink) {
            stub.name?.let {
                sink.occurrence(FortranNamedElementIndex.KEY, it)
            }
        }
    }
}