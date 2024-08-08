package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl
import org.jetbrains.fortran.lang.stubs.FortranProgramUnitStub

abstract class FortranModuleImplMixin : FortranProgramUnitImpl, FortranModule {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = moduleStmt.entityDecl

    override val variables: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranTypeDeclarationStmt::class.java)
                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEntityDecl::class.java) }

    override val unit: FortranNamedElement?
        get() {
            val moduleStmt = PsiTreeUtil.getStubChildOfType(this, FortranModuleStmt::class.java)
            return PsiTreeUtil.getStubChildOfType(moduleStmt, FortranEntityDecl::class.java)
        }

    override val subprograms: List<FortranNamedElement>
        get() {
            val programUnits = PsiTreeUtil.getStubChildrenOfTypeAsList(moduleSubprogramPart, FortranProgramUnit::class.java)
            val functionsDeclarations = programUnits
                    .filterIsInstance(FortranFunctionSubprogram::class.java)
                    .flatMap { function ->
                        function.variables.filter { function.unit?.name.equals(it.name, true) }
                    }
            return programUnits.mapNotNull { it.unit } + functionsDeclarations
        }

    override val usedModules: List<FortranDataPath>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranUseStmt::class.java)
                .mapNotNull { it.dataPath }

    override val types: List<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranDerivedTypeDef::class.java)
                .mapNotNull { it.derivedTypeStmt.typeDecl }

}