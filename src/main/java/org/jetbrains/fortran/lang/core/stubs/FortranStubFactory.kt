package org.jetbrains.fortran.lang.core.stubs

import org.jetbrains.fortran.lang.psi.FortranRenameStmt
import org.jetbrains.fortran.lang.psi.impl.*

fun fortranStubFactory(name: String): FortranStubElementType<*, *> = when (name) {
// program units
    "MAIN_PROGRAM" -> FortranProgramUnitStub.Type("MAIN PROGRAM", ::FortranMainProgramImpl)
    "FUNCTION_SUBPROGRAM" -> FortranProgramUnitStub.Type("FUNCTION", ::FortranFunctionSubprogramImpl)
    "SUBROUTINE_SUBPROGRAM" -> FortranProgramUnitStub.Type("SUBROUTINE", ::FortranSubroutineSubprogramImpl)
    "MODULE" -> FortranProgramUnitStub.Type("MODULE", ::FortranModuleImpl)
    "SUBMODULE" -> FortranProgramUnitStub.Type("SUBMODULE", ::FortranSubmoduleImpl)
    "BLOCK_DATA" -> FortranProgramUnitStub.Type("BLOCK DATA", ::FortranBlockDataImpl)
    "SEPARATE_MODULE_SUBPROGRAM" -> FortranProgramUnitStub.Type("SEPARATE_MODULE_SUBPROGRAM", ::FortranSeparateModuleSubprogramImpl)
    "PROGRAM_UNIT" -> FortranProgramUnitStub.Type("UNKNOWN PROGRAM UNIT", ::FortranProgramUnitImpl)

    "BLOCK" -> FortranBlockStub.Type
    "MODULE_SUBPROGRAM_PART" -> FortranModuleSubprogramPartStub.Type

    // Statements
    // I do not know, how many of them we really need
    "TYPE_DECLARATION_STMT" -> FortranStatementStub.Type("TYPE_DECLARATION_STMT", ::FortranTypeDeclarationStmtImpl)
    "USE_STMT" -> FortranStatementStub.Type("USE_STMT", ::FortranUseStmtImpl)

    // Entity Decl
    "ENTITY_DECL" -> FortranEntityDeclStub.Type("ENTITY_DECL", ::FortranEntityDeclImpl)
    "TYPE_DECL" -> FortranEntityDeclStub.Type("TYPE_DECL", ::FortranTypeDeclImpl)
    "DERIVED_TYPE_DEF" -> FortranDerivedTypeDefStub.Type

    // Use stmt-parts
    "RENAME_STMT" -> FortranRenameStmtStub.Type
    "ONLY_STMT" -> FortranOnlyStmtStub.Type

    // Data path (for rename only)
    "DATA_PATH" -> FortranDataPathStub.Type("DATA_PATH", ::FortranDataPathImpl)
    "TYPE_NAME" -> FortranDataPathStub.Type("TYPE_NAME", ::FortranTypeNameImpl)
    else -> error("Unknown element $name")
}

