package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl
import org.jetbrains.fortran.lang.stubs.FortranProgramUnitStub

abstract class FortranBlockDataImplMixin : FortranProgramUnitImpl, FortranBlockData {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)


    override fun getNameIdentifier(): PsiElement? = blockDataStmt.entityDecl

    override val variables: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranTypeDeclarationStmt::class.java)
                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEntityDecl::class.java) }

    override val unit: FortranNamedElement?
        get() {
            val blockDataStmt = PsiTreeUtil.getStubChildOfType(this, FortranBlockDataStmt::class.java)
            return PsiTreeUtil.getStubChildOfType(blockDataStmt, FortranEntityDecl::class.java) as FortranNamedElement
        }

    override val types: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranDerivedTypeDef::class.java)
                .map{ it.derivedTypeStmt.typeDecl }

    override val usedModules: List<FortranDataPath>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranUseStmt::class.java)
                .mapNotNull{ it.dataPath }
}