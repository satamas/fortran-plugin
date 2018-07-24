package org.jetbrains.fortran.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.lang.psi.ext.descendantOfTypeStrict

class FortranPsiFactory(private val project: Project) {
    fun createFile(text: CharSequence): FortranFile = PsiFileFactory.getInstance(project)
            .createFileFromText("DUMMY.f.95", FortranFileType, text) as FortranFile

    private inline fun <reified T : FortranCompositeElement> createFromText(code: String): T? =
            createFile(code).descendantOfTypeStrict()

    fun createIncludeStmt(path: String): FortranIncludeStmt = createFromText("include '$path';")!!
}