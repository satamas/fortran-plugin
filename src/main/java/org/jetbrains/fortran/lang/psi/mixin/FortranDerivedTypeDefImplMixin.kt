package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranDerivedTypeDef
import org.jetbrains.fortran.lang.psi.FortranEntityDecl
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl

abstract class FortranDerivedTypeDefImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranDerivedTypeDef {
    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(node.psi, FortranEntityDecl::class.java).toTypedArray()

    override val subprograms: Array<FortranNamedElement>
        get() = emptyArray()

    override val unit : FortranNamedElement? = null

    override val usedModules: Array<FortranDataPath>
        get() = emptyArray()

    override val types: Array<FortranNamedElement>
        get() = emptyArray()
}