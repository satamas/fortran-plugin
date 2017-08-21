package org.jetbrains.fortran.lang.psi.mixin

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.psi.impl.FortranProgramUnitImpl

abstract class FortranMainProgramImplMixin(node : ASTNode) : FortranProgramUnitImpl(node), FortranMainProgram {
    override fun getNameIdentifier(): PsiElement? = programStmt?.entityDecl

    override val variables: Array<FortranNamedElement>
        get() {
            val n = PsiTreeUtil.findChildrenOfType(block, FortranEntityDecl::class.java)
            if (programStmt != null)        n.add((programStmt as FortranProgramStmt).entityDecl)
            return n.toTypedArray<FortranNamedElement>()
        }
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