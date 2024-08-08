package org.jetbrains.fortran.lang.types.infer

import org.jetbrains.annotations.TestOnly
import org.jetbrains.fortran.lang.psi.FortranExpr
import org.jetbrains.fortran.lang.types.ty.FortranType
import org.jetbrains.fortran.lang.types.ty.FortranUnknownType
import org.jetbrains.fortran.lang.utils.FortranDiagnostic

class FortranInferenceResult(
        private val exprTypes: Map<FortranExpr, FortranType>,
        val diagnostics: List<FortranDiagnostic>) {

    fun getExprType(expr: FortranExpr): FortranType =
            exprTypes[expr] ?: FortranUnknownType

    override fun toString(): String =
            "FortranInferenceResult(exprTypes=$exprTypes)"

    @TestOnly
    fun isExprTypeInferred(expr: FortranExpr): Boolean =
            expr in exprTypes

}

