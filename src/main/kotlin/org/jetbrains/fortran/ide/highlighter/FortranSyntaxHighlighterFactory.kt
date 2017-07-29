package org.jetbrains.fortran.ide.highlighter

import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class FortranSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    // @NotNull and @Nullable how they work here ???
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) = FortranHighLighter(false)
}
