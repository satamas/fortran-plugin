/*
 * Copyright 2012-2015 Sergey Ignatov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.fortran.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import org.jetbrains.fortran.lang.psi.FortranCompositeElement

object ResolveUtil {
    fun treeWalkUp(place: PsiElement?, processor: PsiScopeProcessor): Boolean {
        var lastParent: PsiElement? = null
        var run = place
        while (run != null) {
            if (place !== run && !run.processDeclarations(
                    processor,
                    ResolveState.initial(),
                    lastParent,
                    place!!
                )
            ) return false
            lastParent = run
            run = run.parent
        }
        return true
    }

    fun processChildren(
        element: PsiElement,
        processor: PsiScopeProcessor,
        substitutor: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        var run = if (lastParent == null) element.lastChild else lastParent.prevSibling
        while (run != null) {
            if (run is FortranCompositeElement && !run.processDeclarations(
                    processor,
                    substitutor,
                    null,
                    place
                )
            ) return false
            run = run.prevSibling
        }
        return true
    }
}
