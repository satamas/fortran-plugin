package org.jetbrains.fortran.lang.types

import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner
import org.jetbrains.fortran.lang.types.infer.FortranInferenceResult
import org.jetbrains.fortran.lang.types.infer.inferTypesIn

val FortranEntitiesOwner.inference: FortranInferenceResult
    get() = CachedValuesManager.getCachedValue(this, {
        CachedValueProvider.Result.create(inferTypesIn(this), PsiModificationTracker.MODIFICATION_COUNT)
    })