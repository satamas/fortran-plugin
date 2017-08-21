package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranSubroutineSubprogramImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranNamedElement, FortranSubroutineSubprogram {
    override fun getNameIdentifier(): PsiElement? = subroutineStmt.entityDecl


    override val variables: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
                .plus(subroutineStmt.entityDecl as FortranNamedElement)
                .toTypedArray()

    override val unit: FortranNamedElement
        get() = (subroutineStmt.entityDecl as FortranNamedElement)

    override val subprograms: Array<FortranNamedElement>
        get() = PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranProgramUnit::class.java)
                .map{ it -> (it.firstChild as FortranNameStmt).entityDecl as FortranNamedElement}
                .plus(PsiTreeUtil.findChildrenOfType(internalSubprogramPart, FortranFunctionSubprogram::class.java)
                        .flatMap { function ->
                            PsiTreeUtil.findChildrenOfType((function as FortranFunctionSubprogram).block, FortranEntityDecl::class.java).filter { it.name == function.name  }
                        }.filterNotNull())
                .toTypedArray()
}