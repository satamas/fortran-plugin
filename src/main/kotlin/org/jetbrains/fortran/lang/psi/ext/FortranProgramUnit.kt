package org.jetbrains.fortran.lang.psi.ext

import org.jetbrains.fortran.lang.psi.*


val FortranProgramUnit.beginUnitStmt: FortranStmt? get() {
    return when (this) {
        is FortranMainProgram -> this.programStmt
        is FortranBlockData -> this.blockDataStmt
        is FortranSubmodule -> this.submoduleStmt
        is FortranModule -> this.moduleStmt
        is FortranFunctionSubprogram -> this.functionStmt
        is FortranSubroutineSubprogram -> this.subroutineStmt
        else -> null
    }
}

val FortranProgramUnit.endUnitStmt: FortranStmt? get() {
    return when (this) {
        is FortranMainProgram -> this.endProgramStmt
        is FortranBlockData -> this.endBlockDataStmt
        is FortranSubmodule -> this.endSubmoduleStmt
        is FortranModule -> this.endModuleStmt
        is FortranFunctionSubprogram -> this.endFunctionStmt
        is FortranSubroutineSubprogram -> this.endSubroutineStmt
        else -> null
    }
}

