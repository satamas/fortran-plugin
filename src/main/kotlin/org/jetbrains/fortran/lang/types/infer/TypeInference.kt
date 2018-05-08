package org.jetbrains.fortran.lang.types.infer

import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner

fun inferTypesIn(element: FortranEntitiesOwner): FortranInferenceResult {
    return FortranInferenceResult(emptyList())
}