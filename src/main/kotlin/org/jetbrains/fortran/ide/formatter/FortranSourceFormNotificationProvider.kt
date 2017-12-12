package org.jetbrains.fortran.ide.formatter

import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import org.jetbrains.fortran.ide.formatter.settings.FortranCodeStyleSettings
import org.jetbrains.fortran.ide.intentions.FortranFixedFormConverter
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile


class FortranSourceFormNotificationProvider(private val myProject: Project) : EditorNotifications.Provider<EditorNotificationPanel>() {
    private val KEY = Key.create<EditorNotificationPanel>("FixedFormSource")

    override fun getKey(): Key<EditorNotificationPanel> {
        return KEY
    }

    override fun createNotificationPanel(file: VirtualFile, fileEditor: FileEditor): EditorNotificationPanel? {
        if (userDoesntLikeUs || !FortranCodeStyleSettings.SHOW_SOURCE_FORM_CONVERTER_PANEL) return null
        val psiFile = (PsiManager.getInstance(myProject).findFile(file) ?: return null) as? FortranFixedFormFile ?: return null

        return FortranSourceFormNotificationPanel(myProject, psiFile)
    }

    private class FortranSourceFormNotificationPanel(project: Project,
                                                     file: FortranFixedFormFile) : EditorNotificationPanel() {
        init {
            setText("Fixed source form is deprecated. Translate file to free form?")

            createActionLabel("Translate") { translate(project, file) }.toolTipText = "Translate file"
            createActionLabel("Hide") { hideNotification(project) }.toolTipText = "Hide this notification"
            createActionLabel("Don't show again") { killNotifications(project) }.toolTipText = "Don't show this message again"
        }

        private fun hideNotification(project: Project) {
            isVisible = false
            userDoesntLikeUs = true
            EditorNotifications.getInstance(project).updateAllNotifications()
        }

        private fun killNotifications(project: Project) {
            isVisible = false
            userDoesntLikeUs = true
            FortranCodeStyleSettings.SHOW_SOURCE_FORM_CONVERTER_PANEL = false
            EditorNotifications.getInstance(project).updateAllNotifications()
        }

        private fun translate(project: Project, file: FortranFixedFormFile) {
            CommandProcessor.getInstance().executeCommand(project,
                    { FortranFixedFormConverter().invoke(project, null, file) },
                    "Translate source to free form", null)
        }
    }

    companion object {
        private var userDoesntLikeUs = false
    }
}
