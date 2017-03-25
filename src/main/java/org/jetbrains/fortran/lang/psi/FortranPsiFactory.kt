package org.jetbrains.fortran.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.lang.psi.ext.childOfType

class FortranPsiFactory(private val project: Project) {
    fun createLabel(text: String): PsiElement =
        createFromText<FortranLabel>("$text program\nend")?.integerLiteral
            ?: error("Failed to create quote identifier: `$text`")

    private inline fun <reified T : FortranCompositeElement> createFromText(code: String): T? =
        PsiFileFactory.getInstance(project)
            .createFileFromText("DUMMY.rs", FortranFileType.INSTANCE, code)
            .childOfType<T>()
}