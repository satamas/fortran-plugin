package org.jetbrains.fortran.lang.parser

import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.fortran.lang.stubs.FortranFileStub
import org.jetbrains.fortran.lang.lexer.FortranLexer
import org.jetbrains.fortran.lang.psi.FortranFile

class FortranParserDefinition : FortranParserDefinitionBase() {
    override fun createLexer(project: Project): Lexer {
        return FortranLexer(false)
    }

    override fun getFileNodeType(): IFileElementType {
        return FortranFileStub.Type
    }

    override fun createFile(fileViewProvider: FileViewProvider): PsiFile {
        return FortranFile(fileViewProvider)
    }
}