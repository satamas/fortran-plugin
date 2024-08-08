package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranEnumDef
import org.jetbrains.fortran.lang.psi.impl.FortranEnumDefImpl

class FortranEnumDefStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranEnumDef>(parent, elementType) {
    object Type : FortranStubElementType<FortranEnumDefStub, FortranEnumDef>("ENUM_DEF") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranEnumDefStub =
                FortranEnumDefStub(parentStub, this)

        override fun serialize(stub: FortranEnumDefStub, dataStream: StubOutputStream) {

        }

        override fun createPsi(stub: FortranEnumDefStub) = FortranEnumDefImpl(stub, this)

        override fun createStub(psi: FortranEnumDef, parentStub: StubElement<*>?) =
                FortranEnumDefStub(parentStub, this)

        override fun indexStub(stub: FortranEnumDefStub, sink: IndexSink) {

        }
    }
}