package org.jetbrains.fortran.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.fortran.FortranLanguage
import org.jetbrains.fortran.lang.parser.FortranFileParser

class FortranFileElementType : IFileElementType(FortranLanguage) {
    override fun parseContents(chameleon: ASTNode): ASTNode? {
        return FortranFileParser.parse(this, chameleon)
    }
}
