package org.jetbrains.fortran.ide.findUsages

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl
import org.jetbrains.fortran.lang.psi.FortranLabelDecl
import java.lang.IllegalArgumentException

class FortranFindUsagesHandlerFactory(project: Project) : FindUsagesHandlerFactory() {
    override fun canFindUsages(element: PsiElement): Boolean = element is FortranLabelDecl

    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler {
        when (element) {
            is FortranLabelDecl->
                return FortranLabelDeclFindUsagesHandler(element, this)
            is FortranConstructNameDecl ->
                return FortranConstructNameDeclFindUsagesHandler(element, this)
            else ->
                throw IllegalArgumentException("unexpected element type: $element")
        }
    }
}