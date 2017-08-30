package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.fortran.lang.FortranTypes
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl
import org.jetbrains.fortran.lang.FortranTypes.IDENTIFIER
import org.jetbrains.fortran.lang.psi.FortranConstructNameDecl

abstract class FortranConstructNameDeclImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranConstructNameDecl {
    override fun getNameIdentifier(): PsiElement = notNullChild(findChildByType(IDENTIFIER))

    override fun setName(name: String): PsiElement? {
        val keyNode : LeafPsiElement = findNotNullChildByType(FortranTypes.IDENTIFIER)
        node.replaceChild(keyNode.node, LeafPsiElement(FortranTypes.IDENTIFIER, name).node)
        return this
    }

    fun getLabelValue() = nameIdentifier.text
}