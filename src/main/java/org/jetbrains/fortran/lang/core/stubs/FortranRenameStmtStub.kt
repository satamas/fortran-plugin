package org.jetbrains.fortran.lang.core.stubs

import com.intellij.psi.stubs.*
import org.jetbrains.fortran.lang.psi.FortranRenameStmt
import org.jetbrains.fortran.lang.psi.impl.FortranRenameStmtImpl


class FortranRenameStmtStub(
        parent: StubElement<*>?, elementType: IStubElementType<*, *>
): StubBase<FortranRenameStmt>(parent, elementType){

    object Type: FortranStubElementType<FortranRenameStmtStub, FortranRenameStmt>("RENAME STMT") {

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): FortranRenameStmtStub {
            return FortranRenameStmtStub(parentStub, this)
        }

        override fun serialize(stub: FortranRenameStmtStub, dataStream: StubOutputStream) {
        }

        override fun createPsi(stub: FortranRenameStmtStub) = FortranRenameStmtImpl(stub, this)

        override fun createStub(psi: FortranRenameStmt, parentStub: StubElement<*>?) =
                FortranRenameStmtStub(parentStub, this)

        override fun indexStub(stub: FortranRenameStmtStub, sink: IndexSink) {
            // NOP
        }
    }
}