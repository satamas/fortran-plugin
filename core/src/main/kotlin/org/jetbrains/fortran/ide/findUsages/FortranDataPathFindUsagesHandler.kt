package org.jetbrains.fortran.ide.findUsages

import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import org.jetbrains.fortran.lang.psi.FortranDataPath

class FortranDataPathFindUsagesHandler (
        element: FortranDataPath,
        factory: FortranFindUsagesHandlerFactory
) : FortranFindUsagesHandler<FortranDataPath>(element, factory) {
    override fun calculateScope(element: PsiElement, searchScope: SearchScope): SearchScope
            = runReadAction { GlobalSearchScope.fileScope(element.containingFile) }
}