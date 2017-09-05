package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.core.stubs.FortranEnumDefStub
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl

abstract class FortranEnumDefImplMixin : FortranStubbedElementImpl<FortranEnumDefStub>, FortranEnumDef {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranEnumDefStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(this, FortranEnumeratorDefStmt::class.java)
                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEnumerator::class.java)
                }.map{ it.entityDecl }.toTypedArray()


    override val subprograms: Array<FortranNamedElement>
        get() = emptyArray()

    override val unit : FortranNamedElement? = null

    override val usedModules: Array<FortranDataPath>
        get() = emptyArray()

    override val types: Array<FortranNamedElement>
        get() = emptyArray()
}