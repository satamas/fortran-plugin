package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranEnumDef
import org.jetbrains.fortran.lang.psi.FortranEnumerator
import org.jetbrains.fortran.lang.psi.FortranEnumeratorDefStmt
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedElementImpl
import org.jetbrains.fortran.lang.stubs.FortranEnumDefStub

abstract class FortranEnumDefImplMixin : FortranStubbedElementImpl<FortranEnumDefStub>, FortranEnumDef {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranEnumDefStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(this, FortranEnumeratorDefStmt::class.java)
                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEnumerator::class.java) }
                .map{ it.entityDecl }.toList()


    override val subprograms: List<FortranNamedElement>
        get() = emptyList()

    override val unit : FortranNamedElement? = null

    override val usedModules: List<FortranDataPath>
        get() = emptyList()

    override val types: List<FortranNamedElement>
        get() = emptyList()
}