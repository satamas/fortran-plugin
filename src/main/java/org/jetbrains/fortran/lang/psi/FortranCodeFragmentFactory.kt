package org.jetbrains.fortran.lang.psi


import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project


class FortranCodeFragmentFactory(private val project: Project) {
    private val psiFactory = FortranPsiFactory(project)

    fun createPath(path: String, context: FortranCompositeElement): FortranDataPath? =
            psiFactory.tryCreatePath(path)?.apply {
                val module = ModuleUtilCore.findModuleForPsiElement(context.containingFile)
                containingFile?.putUserData(ModuleUtilCore.KEY_MODULE, module)
            }
}