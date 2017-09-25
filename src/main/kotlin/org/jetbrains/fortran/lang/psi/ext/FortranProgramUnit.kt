package org.jetbrains.fortran.lang.psi.ext

import org.jetbrains.fortran.lang.psi.*


val FortranProgramUnit.beginUnitStmt: FortranBeginUnitStmt? get() {
    return when (this) {
        is FortranMainProgram -> this.programStmt
        is FortranBlockData -> this.blockDataStmt
        is FortranSubmodule -> this.submoduleStmt
        is FortranModule -> this.moduleStmt
        is FortranFunctionSubprogram -> this.functionStmt
        is FortranSubroutineSubprogram -> this.subroutineStmt
        is FortranSeparateModuleSubprogram -> this.mpSubprogramStmt
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
        is FortranSeparateModuleSubprogram -> this.endMpSubprogramStmt
        else -> null
    }
}

val FortranProgramUnit.unitType: String? get() {
    return when (this) {
        is FortranMainProgram -> "program"
        is FortranBlockData -> "block data"
        is FortranSubmodule -> "submodule"
        is FortranModule -> "module"
        is FortranFunctionSubprogram -> "function"
        is FortranSubroutineSubprogram -> "subroutine"
        is FortranSeparateModuleSubprogram -> "procedure"
        else -> null
    }
}

