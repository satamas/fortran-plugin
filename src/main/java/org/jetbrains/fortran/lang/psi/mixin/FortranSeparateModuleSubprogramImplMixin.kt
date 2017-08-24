package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.core.stubs.FortranProgramUnitStub
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranSeparateModuleSubprogramImplMixin : FortranProgramUnitImpl, FortranNamedElement, FortranSeparateModuleSubprogram {

    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = mpSubprogramStmt.entityDecl

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) is FortranProgramUnit }
                .toTypedArray()

    override val unit: FortranNamedElement
        get() = (mpSubprogramStmt.entityDecl as FortranNamedElement)

    override val usedModules: Array<FortranDataPath>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranUseStmt::class.java)
                .map{ it.dataPath }.filterNotNull().toTypedArray()

    override val types: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranDerivedTypeDef::class.java)
                .map{ it.derivedTypeStmt.typeDecl }.filterNotNull().toTypedArray()
}