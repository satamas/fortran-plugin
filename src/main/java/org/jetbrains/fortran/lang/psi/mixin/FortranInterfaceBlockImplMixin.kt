package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.core.stubs.FortranInterfaceBlockStub
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.ext.FortranStubbedNamedElementImpl


abstract class FortranInterfaceBlockImplMixin : FortranStubbedNamedElementImpl<FortranInterfaceBlockStub>, FortranInterfaceBlock {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranInterfaceBlockStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = interfaceStmt.entityDecl

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) stub.name else nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement? {
        return this
    }

    override val variables: Array<FortranNamedElement>
        get() = emptyArray()

    override val subprograms: Array<FortranNamedElement>
        get() {
            if (name != null) {
                return arrayOf(interfaceStmt.entityDecl!!)
            } else {
                return interfaceBodyList.map{ it -> (it.firstChild as FortranNameStmt).entityDecl as FortranNamedElement}
                        .plus(interfaceBodyList.filter{it.firstChild is FortranFunctionStmt }
                                .flatMap { function ->
                                    PsiTreeUtil.findChildrenOfType(function.block, FortranEntityDecl::class.java)
                                            .filter { (function.firstChild as FortranFunctionStmt).entityDecl?.name.equals(it.name, true)  }
                                }.filterNotNull())
                        .plus(procedureStmtList.flatMap { PsiTreeUtil.findChildrenOfType(it, FortranEntityDecl::class.java) })
                        .toTypedArray()
            }
        }

    override val unit : FortranNamedElement? = null

    override val usedModules: Array<FortranDataPath>
        get() = emptyArray()

    override val types: Array<FortranNamedElement>
        get() = emptyArray()
}