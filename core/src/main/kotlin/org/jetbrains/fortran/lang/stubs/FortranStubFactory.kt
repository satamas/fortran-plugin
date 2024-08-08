package org.jetbrains.fortran.lang.stubs

import org.jetbrains.fortran.lang.psi.impl.*

fun fortranStubFactory(name: String): FortranStubElementType<*, *> = when (name) {
// program units
    "MAIN_PROGRAM" -> FortranProgramUnitStub.Type("MAIN_PROGRAM", ::FortranMainProgramImpl)
    "FUNCTION_SUBPROGRAM" -> FortranProgramUnitStub.Type("FUNCTION_SUBPROGRAM", ::FortranFunctionSubprogramImpl)
    "SUBROUTINE_SUBPROGRAM" -> FortranProgramUnitStub.Type("SUBROUTINE_SUBPROGRAM", ::FortranSubroutineSubprogramImpl)
    "MODULE" -> FortranProgramUnitStub.Type("MODULE", ::FortranModuleImpl)
    "SUBMODULE" -> FortranProgramUnitStub.Type("SUBMODULE", ::FortranSubmoduleImpl)
    "BLOCK_DATA" -> FortranProgramUnitStub.Type("BLOCK_DATA", ::FortranBlockDataImpl)
    "SEPARATE_MODULE_SUBPROGRAM" -> FortranProgramUnitStub.Type("SEPARATE_MODULE_SUBPROGRAM", ::FortranSeparateModuleSubprogramImpl)
    "PROGRAM_UNIT" -> FortranProgramUnitStub.Type("PROGRAM_UNIT", ::FortranProgramUnitImpl)

    "BLOCK" -> FortranBlockStub.Type
    "MODULE_SUBPROGRAM_PART" -> FortranModuleSubprogramPartStub.Type
    "INTERNAL_SUBPROGRAM_PART" -> FortranInternalSubprogramPartStub.Type
    "INTERFACE_SPECIFICATION" -> FortranInterfaceSpecificationStub.Type
    // Statements
    // I do not know, how many of them we really need
    "TYPE_DECLARATION_STMT" -> FortranStatementStub.Type("TYPE_DECLARATION_STMT", ::FortranTypeDeclarationStmtImpl)
    "USE_STMT" -> FortranStatementStub.Type("USE_STMT", ::FortranUseStmtImpl)
    "FUNCTION_STMT" -> FortranStatementStub.Type("FUNCTION_STMT", ::FortranFunctionStmtImpl)
    "PROGRAM_STMT" -> FortranStatementStub.Type("PROGRAM_STMT", ::FortranProgramStmtImpl)
    "BLOCK_DATA_STMT" -> FortranStatementStub.Type("BLOCK_DATA_STMT", ::FortranBlockDataStmtImpl)
    "SUBMODULE_STMT" -> FortranStatementStub.Type("SUBMODULE_STMT", ::FortranSubmoduleStmtImpl)
    "MODULE_STMT" -> FortranStatementStub.Type("MODULE_STMT", ::FortranModuleStmtImpl)
    "SUBROUTINE_STMT" -> FortranStatementStub.Type("SUBROUTINE_STMT", ::FortranSubroutineStmtImpl)
    "MP_SUBPROGRAM_STMT" -> FortranStatementStub.Type("MP_SUBPROGRAM_STMT", ::FortranMpSubprogramStmtImpl)

    // Entity Decl
    "ENTITY_DECL" -> _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub.Type(
        "ENTITY_DECL",
        ::FortranEntityDeclImpl
    )

    "TYPE_DECL" -> _root_ide_package_.org.jetbrains.fortran.lang.stubs.FortranEntityDeclStub.Type(
        "TYPE_DECL",
        ::FortranTypeDeclImpl
    )
    "DERIVED_TYPE_DEF" -> FortranDerivedTypeDefStub.Type

    // Use stmt-parts
    "RENAME_STMT" -> FortranRenameStmtStub.Type
    "ONLY_STMT" -> FortranOnlyStmtStub.Type

    // Data path (for rename only)
    "DATA_PATH" -> FortranDataPathStub.Type("DATA_PATH", ::FortranDataPathImpl)
    "TYPE_NAME" -> FortranDataPathStub.Type("TYPE_NAME", ::FortranTypeNameImpl)

    // Interface
    "INTERFACE_BLOCK" -> FortranInterfaceBlockStub.Type

    // ENUM
    "ENUM_DEF" -> FortranEnumDefStub.Type

    else -> error("Unknown element $name")
}

