package org.jetbrains.fortran.lang.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.stubs.index.FortranNamedElementIndex

class FortranEntityDeclStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>,
        override val name: String?
) : StubBase<FortranEntityDecl>(parent, elementType), org.jetbrains.fortran.lang.stubs.FortranNamedStub {
    class Type<out PsiT : FortranEntityDecl>(
            debugName: String,
            private val psiCtor: (org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub, IStubElementType<*, *>) -> PsiT
    ) : org.jetbrains.fortran.lang.stubs.FortranStubElementType<org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub, FortranEntityDecl>(
        debugName
    ) {

        override fun deserialize(
            dataStream: StubInputStream,
            parentStub: StubElement<*>?
        ): _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub =
            _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub(
                parentStub,
                this,
                dataStream.readName()?.string
            )

        override fun serialize(
            stub: _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub,
            dataStream: StubOutputStream
        ) {
            with(dataStream) {
                writeName(stub.name)
            }
        }

        override fun createPsi(stub: _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub) =
            psiCtor(stub, this)

        override fun createStub(psi: FortranEntityDecl, parentStub: StubElement<*>?) =
            _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub(parentStub, this, psi.name)

        override fun indexStub(
            stub: _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub,
            sink: IndexSink
        ) {
            stub.name?.let {
                sink.occurrence(FortranNamedElementIndex.KEY, it)
            }
        }
    }
}