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

abstract class FortranMainProgramImplMixin : FortranProgramUnitImpl, FortranMainProgram {
    constructor(node: ASTNode) : super(node)

    constructor(stub: FortranProgramUnitStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement? = programStmt?.entityDecl

    override val variables: Array<FortranNamedElement>
        get() {
            val n = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                    .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) is FortranProgramUnit }
                    .toMutableList()
            if (programStmt != null)        n.add((programStmt as FortranProgramStmt).entityDecl)
            return n.toTypedArray<FortranNamedElement>()
        }
    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranProgramUnit::class.java)
                .map{ it -> (it.firstChild as FortranNameStmt).entityDecl as FortranNamedElement}
                .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) is FortranProgramUnit }
                .plus(PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranFunctionSubprogram::class.java)
                        .flatMap { function ->
                            PsiTreeUtil.findChildrenOfType((function as FortranFunctionSubprogram).block, FortranEntityDecl::class.java).filter { function.name.equals(it.name, true)  }
                        }.filterNotNull())
                .toTypedArray()

    override val usedModules: Array<FortranDataPath>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranUseStmt::class.java)
                .map{ it.dataPath }.filterNotNull().toTypedArray()

    override val types: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranDerivedTypeDef::class.java)
                .map{ it.derivedTypeStmt.typeDecl }.filterNotNull().toTypedArray()
}