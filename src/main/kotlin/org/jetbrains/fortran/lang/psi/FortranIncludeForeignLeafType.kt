package org.jetbrains.fortran.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.ForeignLeafType
import com.intellij.psi.tree.IElementType

class FortranIncludeForeignLeafType(val type: IElementType, val tokenText: CharSequence) : ForeignLeafType(type, tokenText) {
    override fun createLeafNode(leafText: CharSequence): ASTNode {
        return FortranIncludeForeignLeafElement(this, value)
    }
}