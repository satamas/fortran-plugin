package org.jetbrains.fortran.ide.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import org.jetbrains.fortran.FortranIcons

class FortranCreateFileAction : CreateFileFromTemplateAction(CAPTION, "", FortranIcons.fileTypeIcon),
        DumbAware {
    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = CAPTION

    override fun buildDialog(
            project: Project,
            directory: PsiDirectory,
            builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder.setTitle(CAPTION).addKind("Empty File", FortranIcons.fileTypeIcon, "Fortran File")
                .addKind("Main Program", FortranIcons.mainProgramIcon, "Main Program")
                .addKind("Module", FortranIcons.moduleIcon, "Module")
                .addKind("Function", FortranIcons.functionIcon, "Function")
                .addKind("Subroutine", FortranIcons.subroutineIcon, "Subroutine")
    }

    private companion object {
        private const val CAPTION = "New Fortran File"
    }
}
