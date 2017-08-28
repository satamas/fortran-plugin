package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranStmt

class FortranStatementStub (
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranStmt>(parent, elementType) {
    class Type<PsiT : FortranStmt>(
            debugName: String,
            private val psiCtor: (FortranStatementStub, IStubElementType<*, *>) -> PsiT
    ) : FortranStubElementType<FortranStatementStub, FortranStmt>(debugName) {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranStatementStub =
                FortranStatementStub(parentStub, this)

        override fun serialize(stub: FortranStatementStub, dataStream: StubOutputStream) {}

        override fun createPsi(stub: FortranStatementStub) = psiCtor(stub, this)

        override fun createStub(psi: FortranStmt, parentStub: StubElement<*>?) =
                FortranStatementStub(parentStub, this)

        override fun indexStub(stub: FortranStatementStub, sink: IndexSink) {
            // NOP
        }
    }
}