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

abstract class FortranSubroutineSubprogramImplMixin : FortranProgramUnitImpl, FortranNamedElement, FortranSubroutineSubprogram {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = subroutineStmt.entityDecl


    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranTypeDeclarationStmt::class.java)
                .flatMap { PsiTreeUtil.getStubChildrenOfTypeAsList(it, FortranEntityDecl::class.java) }
                .toTypedArray()

    override val unit: FortranNamedElement
        get() = PsiTreeUtil.getStubChildOfType(
                PsiTreeUtil.getStubChildOfType(this, FortranSubroutineStmt::class.java),
                FortranEntityDecl::class.java) as FortranNamedElement

    override val subprograms: Array<FortranNamedElement>
        get() {
            val programUnits = PsiTreeUtil.getStubChildrenOfTypeAsList(internalSubprogramPart, FortranProgramUnit::class.java)

            return programUnits.map{ it.unit }.filterNotNull()
                    .plus(programUnits.filterIsInstance(FortranFunctionSubprogram::class.java)
                            .flatMap { f -> f.variables.filter { f.unit?.name.equals(it.name, true) } }
                            .filterNotNull())
                    .toTypedArray()
        }

    override val usedModules: Array<FortranDataPath>
        get() = PsiTreeUtil.getStubChildrenOfTypeAsList(block, FortranUseStmt::class.java)
                .map{ it.dataPath }.filterNotNull().toTypedArray()

    override val types: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranDerivedTypeDef::class.java)
                .map{ it.derivedTypeStmt.typeDecl }.filterNotNull().toTypedArray()
}