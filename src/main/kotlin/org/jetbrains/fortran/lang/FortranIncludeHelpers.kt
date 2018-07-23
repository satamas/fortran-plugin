package org.jetbrains.fortran.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceHelper
import com.intellij.psi.search.SpecificNameItemProcessor
import com.intellij.util.SmartList

fun resolveIncludedFile(
        context: VirtualFile?,
        includePath: String,
        project: Project
): VirtualFile? {
    val item = FileReferenceHelper.getPsiFileSystemItem(PsiManager.getInstance(project), context!!)
    val pathElements = includePath.split('/')
    val first = pathElements.first()
    var file = findAbsoluteFile(includePath, first)

    if(file != null) return file

    if (item != null && item.parent != null) {
        val firstSegmentElements = findChild(item.parent, first)
        file = findFile(firstSegmentElements, pathElements)
    }

    return file
}

private fun findAbsoluteFile(path: String, first: String): VirtualFile? {
    if (first.isEmpty() || FileUtil.isWindowsAbsolutePath(path)) {
        val file = LocalFileSystem.getInstance().findFileByPath(path)
        return if (file == null || file.isDirectory) null else file
    }
    return null
}

private fun findFile(firstSegmentElements: List<PsiFileSystemItem>,
                     pathElements: List<String>): VirtualFile? {
    return firstSegmentElements.asSequence()
            .map { if (pathElements.size != 1) find(it, pathElements, 1) else it as? PsiFile }
            .map { it?.virtualFile }
            .filterNotNull()
            .firstOrNull()
}

private fun find(root: PsiFileSystemItem?, pathElements: List<String>, cur: Int): PsiFile? {
    if (root == null || cur >= pathElements.size) return null

    val name = pathElements[cur]
    if (name.isEmpty()) {
        return find(root, pathElements, cur + 1)
    }

    for (item in findChild(root, name)) {
        if (cur == pathElements.size - 1) {
            if (item is PsiFile) return item
        } else {
            val element = find(item, pathElements, cur + 1)
            if (element != null) {
                return element
            }
        }
    }
    return null
}

private fun findChild(parent: PsiFileSystemItem?, childName: String): List<PsiFileSystemItem> {
    if (parent == null || !parent.isValid) return emptyList()

    return when (childName) {
        ".." -> {
            val vFile = parent.virtualFile ?: return emptyList()
            val vParent = vFile.parent ?: return emptyList()
            val directory = parent.manager.findDirectory(vParent)
            if (directory != null) listOf(directory) else emptyList()
        }
        "." -> listOf(parent)
        else -> {
            if (parent !is PsiDirectory) {
                val result = SmartList<PsiFileSystemItem>()
                parent.processChildren(object : SpecificNameItemProcessor(childName) {
                    override fun execute(element: PsiFileSystemItem): Boolean {
                        result.add(element)
                        return true
                    }
                })
                result
            } else {
                // special-case optimization: iterating over directories and getting file names is expensive
                findInDir((parent as PsiDirectory?)!!, childName)
            }
        }
    }
}

private fun findInDir(dir: PsiDirectory, childName: String): List<PsiFileSystemItem> {
    val found: PsiFileSystemItem? = dir.findFile(childName) ?: dir.findSubdirectory(childName)
    return if (found != null) listOf(found) else emptyList()
}