package org.jetbrains.fortran.ide.findUsages

import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*

class FortranEntityDeclFindUsagesHandler (
        element: FortranEntityDecl,
        factory: FortranFindUsagesHandlerFactory
) : FortranFindUsagesHandler<FortranEntityDecl>(element, factory) {

    override fun calculateScope(element: PsiElement, searchScope: SearchScope): SearchScope {
        var scope : SearchScope = searchScope
        val unit = runReadAction{PsiTreeUtil.getParentOfType(element, FortranProgramUnit::class.java)}
        val inter = runReadAction{PsiTreeUtil.getParentOfType(element, FortranInterfaceSpecification::class.java)}
        if ((runReadAction{ element.parent !is FortranBeginUnitStmt } && (unit !is FortranModule)
                && (unit !is FortranSubmodule)) || inter != null) {
            scope = runReadAction { GlobalSearchScope.fileScope(element.containingFile) }
        }
        return scope
    }
}