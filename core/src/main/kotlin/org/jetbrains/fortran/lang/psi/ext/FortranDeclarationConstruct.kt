package org.jetbrains.fortran.lang.psi.ext

import org.jetbrains.fortran.lang.psi.FortranDeclarationConstruct
import org.jetbrains.fortran.lang.psi.FortranDerivedTypeDef
import org.jetbrains.fortran.lang.psi.FortranEnumDef
import org.jetbrains.fortran.lang.psi.FortranInterfaceBlock

val FortranDeclarationConstruct.beginConstructStatement
    get() = when (this) {
        is FortranDerivedTypeDef -> this.derivedTypeStmt
        is FortranEnumDef -> this.enumDefStmt
        is FortranInterfaceBlock -> this.interfaceStmt
        else -> null
    }

val FortranDeclarationConstruct.endConstructStatement
    get() = when (this) {
        is FortranDerivedTypeDef -> this.endTypeStmt
        is FortranEnumDef -> this.endEnumStmt
        is FortranInterfaceBlock -> this.endInterfaceStmt
        else -> null
    }