package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import org.jetbrains.fortran.lang.core.stubs.FortranProgramUnitStub
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranProgramUnit
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl

abstract class FortranProgramUnitImplMixin : FortranStubbedNamedElementImpl<FortranProgramUnitStub>, FortranProgramUnit {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

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