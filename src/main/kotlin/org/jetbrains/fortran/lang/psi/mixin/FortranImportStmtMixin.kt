package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.util.IncorrectOperationException
import com.intellij.util.indexing.IndexingDataKeys
import org.jetbrains.fortran.lang.psi.FortranIncludeStmt
import org.jetbrains.fortran.lang.psi.impl.FortranCompositeElementImpl
import org.jetbrains.fortran.lang.resolveIncludedFile

abstract class FortranIncludeStmtMixin(node: ASTNode) : FortranCompositeElementImpl(node), FortranIncludeStmt {
    override fun getReferences(): Array<out PsiReference?> = createRefSet()?.allReferences ?: emptyArray()

    private fun createRefSet(): FileReferenceSet? {
        val fileNameElement = stringliteral ?: return null
        val path = getPath() ?: return null
        return object : FileReferenceSet(path, this, fileNameElement.startOffsetInParent + 1,
                null, SystemInfo.isFileSystemCaseSensitive, true) {
            override fun createFileReference(range: TextRange, index: Int, text: String): FileReference {
                return object : FileReference(this, range, index, text) {
                    override fun innerResolve(caseSensitive: Boolean, containingFile: PsiFile): Array<ResolveResult> {
                        val targetFile = getTargetFile() ?: return emptyArray()
                        val targetPsiFile = PsiManager.getInstance(project).findFile(targetFile) ?: return emptyArray()
                        return arrayOf(PsiElementResolveResult(targetPsiFile))
                    }
                }
            }
        }
    }

    open fun getPath(): String? {
        val fileNameElement = stringliteral ?: return null
        return fileNameElement.text.trim('\'', '"')
    }

    open fun getTargetFile(): VirtualFile? {
        val virtualFile = containingFile?.originalFile?.virtualFile
                ?: containingFile?.getUserData(IndexingDataKeys.VIRTUAL_FILE)
        return resolveIncludedFile(virtualFile, getPath(), project)
    }
}