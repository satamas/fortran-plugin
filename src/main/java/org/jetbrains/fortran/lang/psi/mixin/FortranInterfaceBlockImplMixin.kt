package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.stubs.FortranInterfaceBlockStub
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl


abstract class FortranInterfaceBlockImplMixin : FortranStubbedNamedElementImpl<FortranInterfaceBlockStub>, FortranInterfaceBlock {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranInterfaceBlockStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = interfaceStmt.entityDecl

    override fun getName(): String? = stub?.name ?: nameIdentifier?.text

    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: List<FortranNamedElement>
        get() = emptyList()

    override val subprograms: List<FortranNamedElement>
        get() {
            if (name != null) {
                return listOf(interfaceStmt.entityDecl!!)
            }
            val functionsAndSubroutines = interfaceBodyList
                    .mapNotNull{((it.functionStmt ?: it.subroutineStmt) as FortranNameStmt).entityDecl}

            val functionsDecls = interfaceBodyList.filter{ it.functionStmt != null }
                    .flatMap { function ->
                        PsiTreeUtil.getStubChildrenOfTypeAsList(function.block, FortranTypeDeclarationStmt::class.java)
                                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEntityDecl::class.java) }
                                .filter { (function.firstChild as FortranFunctionStmt).entityDecl?.name.equals(it.name, true)  }
                    }
            val procedures = procedureStmtList.flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEntityDecl::class.java) }

            return functionsAndSubroutines.plus(functionsDecls).plus(procedures)
        }

    override val unit : FortranNamedElement? = null

    override val usedModules: List<FortranDataPath>
        get() = emptyList()

    override val types: List<FortranNamedElement>
        get() = emptyList()
}