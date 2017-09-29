package org.jetbrains.fortran.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.FortranFileType

class FortranPsiFactory(private val project: Project) {
    fun tryCreatePath(text: String): FortranDataPath? {
        val path = createFromText<FortranDataPath>("${text} = 0; end") ?: return null
        if (path.text != text) return null
        return path
    }

    private inline fun <reified T : FortranCompositeElement> createFromText(code: String): T? =
            PsiTreeUtil.findChildOfType(PsiFileFactory.getInstance(project)
                    .createFileFromText("DUMMY.f95", FortranFileType, code), T::class.java)
}