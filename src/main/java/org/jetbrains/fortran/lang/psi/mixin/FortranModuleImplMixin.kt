package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranEntitiesOwner
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranModuleImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranModule {
    override fun getNameIdentifier(): PsiElement? = moduleStmt.entityDecl


    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .filter{ PsiTreeUtil.getParentOfType(it, FortranEntitiesOwner::class.java) !is FortranProgramUnit }
                .plus(moduleStmt.entityDecl as FortranNamedElement)
                .toTypedArray()

    override val unit: FortranNamedElement
        get() = (moduleStmt.entityDecl as FortranNamedElement)

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(moduleSubprogramPart, FortranProgramUnit::class.java)
                .map{ it -> (it.firstChild as FortranNameStmt).entityDecl as FortranNamedElement}
                .plus(PsiTreeUtil.findChildrenOfType(moduleSubprogramPart, FortranFunctionSubprogram::class.java)
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