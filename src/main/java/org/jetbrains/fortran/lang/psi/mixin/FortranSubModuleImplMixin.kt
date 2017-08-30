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

abstract class FortranSubModuleImplMixin : FortranProgramUnitImpl, FortranSubmodule {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = submoduleStmt.entityDecl

    override fun getName(): String? {
        val stub = stub
        if (stub != null) return stub.name
        val moduleNamePsi = submoduleStmt.parentIdentifier?.firstChild as FortranDataPath?
        val submoduleNamePsi = submoduleStmt.parentIdentifier?.lastChild as FortranDataPath?
        if (moduleNamePsi == null) return nameIdentifier?.text
        else {
            if (submoduleNamePsi == null || submoduleNamePsi == moduleNamePsi) {
                return moduleNamePsi.referenceName + ":" + nameIdentifier?.text
            } else {
                return moduleNamePsi.referenceName + ":" + submoduleNamePsi.referenceName + ":" + nameIdentifier?.text
            }
        }
    }

    fun getModuleName() : String? = name?.substringBefore(':')

    fun getSubModuleName() : String? =
            if (name?.count { it == ':' } == 2)
                name?.substringAfter(':')?.substringBeforeLast(':')
            else null


    fun getPersonalName() : String? = if (name?.contains(':', true) ?: false) name?.substringAfterLast(':') else null

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) is FortranProgramUnit }
                .plus(submoduleStmt.entityDecl as FortranNamedElement)
                .toTypedArray()

    override val unit: FortranNamedElement
        get() = (submoduleStmt.entityDecl as FortranNamedElement)

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(moduleSubprogramPart, FortranProgramUnit::class.java)
                .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) !is FortranProgramUnit }
                .map{ it -> (it.firstChild as FortranNameStmt).entityDecl as FortranNamedElement}
                .plus(PsiTreeUtil.findChildrenOfType(moduleSubprogramPart, FortranFunctionSubprogram::class.java)
                        .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) !is FortranProgramUnit }
                        .flatMap { function ->
                            PsiTreeUtil.findChildrenOfType((function as FortranFunctionSubprogram).block, FortranEntityDecl::class.java).filter { function.name.equals(it.name, true) }
                        }.filterNotNull())
                .toTypedArray()

    override val types: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranDerivedTypeDef::class.java)
                .map{ it.derivedTypeStmt.typeDecl }.filterNotNull().toTypedArray()
}
