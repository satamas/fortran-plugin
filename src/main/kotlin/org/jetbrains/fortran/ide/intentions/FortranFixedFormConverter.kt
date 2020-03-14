package org.jetbrains.fortran.ide.intentions

import com.intellij.codeInsight.FileModificationService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.PsiFileImplUtil
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerImpl
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.ide.util.executeWriteCommand
import org.jetbrains.fortran.lang.psi.FortranFixedFormFile
import org.jetbrains.fortran.lang.psi.FortranTokenType

class FortranFixedFormConverter : AnAction("Convert to Free Form Source") {
    override fun actionPerformed(event: AnActionEvent) {
        val files = selectedFortranFixedFormFiles(event).filter { it.isWritable }.toList()
        val project = CommonDataKeys.PROJECT.getData(event.dataContext)!!

        project.executeWriteCommand("Convert files from fixed to free form", null) {
            files.forEach { file ->
                if (!FileModificationService.getInstance().preparePsiElementForWrite(file)) return@forEach
                val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return@forEach
                document.setText(convertCode(document.charsSequence, file))
                val newFile = PsiFileImplUtil.setName(file, file.name.substringBeforeLast('.') + "." + FortranFileType.defaultExtension)
                PsiDocumentManager.getInstance(project).commitAllDocuments()
                CodeStyleManagerImpl(project).reformatText(newFile, 0, newFile.textLength)
            }
        }
    }

    private fun selectedFortranFixedFormFiles(e: AnActionEvent): Sequence<FortranFixedFormFile> {
        val virtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY) ?: return sequenceOf()
        val project = e.project ?: return sequenceOf()
        return allFortranFixedFormFiles(virtualFiles, project)
    }

    private fun allFortranFixedFormFiles(filesOrDirs: Array<VirtualFile>, project: Project): Sequence<FortranFixedFormFile> {
        val manager = PsiManager.getInstance(project)
        return allFiles(filesOrDirs)
                .asSequence()
                .mapNotNull { manager.findFile(it) as? FortranFixedFormFile }
    }

    private fun allFiles(filesOrDirs: Array<VirtualFile>): Collection<VirtualFile> {
        val result = ArrayList<VirtualFile>()
        for (file in filesOrDirs) {
            VfsUtilCore.visitChildrenRecursively(file, object : VirtualFileVisitor<Unit>() {
                override fun visitFile(file: VirtualFile): Boolean {
                    result.add(file)
                    return true
                }
            })
        }
        return result
    }


    private fun convertCode(codeInFixedForm: CharSequence, file: PsiFile): CharSequence {
        if (codeInFixedForm.isEmpty()) return ""
        return StringBuilder().apply {
            var positionInFixedForm = 0
            PsiTreeUtil.findChildrenOfType(file, PsiComment::class.java).forEach {
                positionInFixedForm = if (it.tokenType == FortranTokenType.LINE_COMMENT) {
                    append(codeInFixedForm.subSequence(positionInFixedForm, it.textOffset))
                    append("!")
                    it.textOffset + 1
                } else {
                    val prevSibling = it.prevSibling
                    if (prevSibling is PsiComment && prevSibling.tokenType == FortranTokenType.LINE_COMMENT) {
                        insert(length - 1, "&")
                        append(codeInFixedForm.subSequence(positionInFixedForm, it.textOffset))
                        append("\n")
                    } else {
                        append(codeInFixedForm.subSequence(positionInFixedForm, it.textOffset))
                        append("&\n")
                    }
                    it.textRange.endOffset
                }
            }
            append(codeInFixedForm.subSequence(positionInFixedForm, codeInFixedForm.length))
        }
    }
}
