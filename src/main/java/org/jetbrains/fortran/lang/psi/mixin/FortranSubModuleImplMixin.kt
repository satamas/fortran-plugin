package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.stubs.FortranProgramUnitStub
import org.jetbrains.fortran.lang.psi.*
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
                ?: return nameIdentifier?.text
        val submoduleNamePsi = submoduleStmt.parentIdentifier?.lastChild as FortranDataPath?

        return if (submoduleNamePsi == null || submoduleNamePsi == moduleNamePsi) {
            moduleNamePsi.referenceName + ":" + nameIdentifier?.text
        } else {
            moduleNamePsi.referenceName + ":" + submoduleNamePsi.referenceName + ":" + nameIdentifier?.text
        }
    }

    fun getModuleName() : String? = name?.substringBefore(':')

    fun getSubModuleName() : String? =
            if (name?.count { it == ':' } == 2)
                name?.substringAfter(':')?.substringBeforeLast(':')
            else null


    fun getPersonalName() : String? = if (name?.contains(':', true) == true) name?.substringAfterLast(':') else null

    override val variables: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranTypeDeclarationStmt::class.java)
                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEntityDecl::class.java) }

    override val unit: FortranNamedElement
        get() {
            val submoduleStmt = PsiTreeUtil.getStubChildOfType(this, FortranSubmoduleStmt::class.java)
            return PsiTreeUtil.getStubChildOfType(submoduleStmt, FortranEntityDecl::class.java) as FortranNamedElement
        }


    override val subprograms: List<FortranNamedElement>
        get() {
            val programUnits = PsiTreeUtil.getStubChildrenOfTypeAsList(moduleSubprogramPart, FortranProgramUnit::class.java)
            val functionsDeclarations = programUnits.filterIsInstance(FortranFunctionSubprogram::class.java)
                    .flatMap { function ->
                        function.variables.filter { function.unit?.name.equals(it.name, true) }
                    }

            return programUnits.mapNotNull{ it.unit } + functionsDeclarations
        }

    override val types: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranDerivedTypeDef::class.java)
                .map{ it.derivedTypeStmt.typeDecl }

    override val usedModules: List<FortranDataPath>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranUseStmt::class.java)
                .mapNotNull{ it.dataPath }
}
