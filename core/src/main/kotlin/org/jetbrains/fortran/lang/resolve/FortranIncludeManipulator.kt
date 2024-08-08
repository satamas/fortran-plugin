package org.jetbrains.fortran.lang.resolve

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil
import org.jetbrains.fortran.lang.psi.FortranIncludeStmt
import org.jetbrains.fortran.lang.psi.FortranPsiFactory

class FortranIncludeManipulator : AbstractElementManipulator<FortranIncludeStmt>() {
    override fun handleContentChange(element: FortranIncludeStmt, range: TextRange, newContent: String?): FortranIncludeStmt? {
        val oldContentNode = element.stringliteral
        if (oldContentNode != null) {
            val newDirective = FortranPsiFactory(element.project).createIncludeStmt(newContent ?: "")
            val newContentNode = newDirective.stringliteral?.node
            CodeEditUtil.replaceChild(element.node, oldContentNode.node, newContentNode!!)
        }
        return element
    }
}