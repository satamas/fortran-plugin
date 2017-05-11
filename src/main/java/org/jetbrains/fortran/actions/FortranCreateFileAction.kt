package org.jetbrains.fortran.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import org.jetbrains.fortran.FortranIcons

class FortranCreateFileAction : CreateFileFromTemplateAction(FortranCreateFileAction.CAPTION, "", FortranIcons.fileTypeIcon),
        DumbAware {

    override fun getActionName(directory: PsiDirectory?, newName: String?, templateName: String?): String = CAPTION

    override fun buildDialog(project: Project?, directory: PsiDirectory?,
                             builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(CAPTION).addKind("Empty File", FortranIcons.fileTypeIcon, "Fortran File")
    }

    private companion object {
        private val CAPTION = "New Fortran File"
    }
}
