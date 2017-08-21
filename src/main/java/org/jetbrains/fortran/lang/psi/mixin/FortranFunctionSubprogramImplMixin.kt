package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranFunctionSubprogramImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranNamedElement, FortranFunctionSubprogram {
    override fun getNameIdentifier(): PsiElement? = functionStmt.entityDecl

    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .plus(functionStmt.entityDecl as FortranNamedElement)
                .toTypedArray<FortranNamedElement>()

    override val unit: FortranNamedElement?
        get() = (functionStmt.entityDecl as FortranNamedElement)

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranProgramUnit::class.java)
                .map{ it -> (it.firstChild as FortranNameStmt).entityDecl as FortranNamedElement}
                .plus(PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranFunctionSubprogram::class.java)
                        .flatMap { function ->
                            PsiTreeUtil.findChildrenOfType((function as FortranFunctionSubprogram).block, FortranEntityDecl::class.java).filter { function.name.equals(it.name, true)  }
                        }.filterNotNull())
                .toTypedArray()

    override val usedModules: Array<FortranDataPath>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranUseStmt::class.java)
                .map{ it.dataPath }.filterNotNull().toTypedArray()
}