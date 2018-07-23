package org.jetbrains.fortran.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilderFactory
import org.jetbrains.fortran.lang.lexer.FortranIncludeProcessingLexer
import org.jetbrains.fortran.lang.psi.FortranFile
import org.jetbrains.fortran.lang.psi.FortranFileElementType

object FortranFileParser : FortranParser() {
    fun parse(root: FortranFileElementType, chameleon: ASTNode): ASTNode? {
        val startElement = chameleon.psi
        val startFile = startElement.containingFile
        val project = startFile.project
        val original = startFile.originalFile
        val context = startFile.context
        val baseFile = if(context != null) context.containingFile else original
        val lexer = FortranIncludeProcessingLexer(baseFile as? FortranFile, project)

        val factory = PsiBuilderFactory.getInstance()
        val builder = factory.createBuilder(project, chameleon, lexer, root.language, chameleon.chars)

        val result = parse(chameleon.elementType, builder)
        return result.firstChildNode
    }
}