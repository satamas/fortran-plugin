package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranInterfaceSpecification
import org.jetbrains.fortran.lang.psi.impl.FortranInterfaceSpecificationImpl


class FortranInterfaceSpecificationStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranInterfaceSpecification>(parent, elementType) {
    object Type :
        FortranStubElementType<FortranInterfaceSpecificationStub, FortranInterfaceSpecification>("INTERFACE_SPECIFICATION") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranInterfaceSpecificationStub =
                FortranInterfaceSpecificationStub(parentStub, this)

        override fun serialize(stub: FortranInterfaceSpecificationStub, dataStream: StubOutputStream) {
        }

        override fun createPsi(stub: FortranInterfaceSpecificationStub) = FortranInterfaceSpecificationImpl(stub, this)

        override fun createStub(psi: FortranInterfaceSpecification, parentStub: StubElement<*>?) =
                FortranInterfaceSpecificationStub(parentStub, this)

        override fun indexStub(stub: FortranInterfaceSpecificationStub, sink: IndexSink) {
        }
    }
}