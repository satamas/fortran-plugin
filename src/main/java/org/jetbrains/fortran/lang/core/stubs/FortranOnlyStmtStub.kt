package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranOnlyStmt
import org.jetbrains.fortran.lang.psi.impl.FortranOnlyStmtImpl


class FortranOnlyStmtStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranOnlyStmt>(parent, elementType){

    object Type: FortranStubElementType<FortranOnlyStmtStub, FortranOnlyStmt>("ONLY_STMT") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranOnlyStmtStub {
            return FortranOnlyStmtStub(parentStub, this)
        }

        override fun serialize(stub: FortranOnlyStmtStub, dataStream: StubOutputStream) {
        }

        override fun createPsi(stub: FortranOnlyStmtStub) = FortranOnlyStmtImpl(stub, this)

        override fun createStub(psi: FortranOnlyStmt, parentStub: StubElement<*>?) =
                FortranOnlyStmtStub(parentStub, this)

        override fun indexStub(stub: FortranOnlyStmtStub, sink: IndexSink) {
            // NOP
        }
    }
}