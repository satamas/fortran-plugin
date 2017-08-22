package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranEnumDef
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElementImpl

abstract class FortranEnumDefImplMixin(node : ASTNode) : FortranNamedElementImpl(node), FortranEnumDef {
    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: Array<FortranNamedElement>
        get() = emptyArray()

    override val subprograms: Array<FortranNamedElement>
        get() = emptyArray()

    override val unit : FortranNamedElement? = null

    override val usedModules: Array<FortranDataPath>
        get() = emptyArray()

    override val types: Array<FortranNamedElement>
        get() = emptyArray()
}