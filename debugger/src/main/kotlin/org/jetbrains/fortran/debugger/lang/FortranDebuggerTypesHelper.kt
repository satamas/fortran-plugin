package org.jetbrains.fortran.debugger.lang

import com.intellij.psi.PsiElement
import com.intellij.xdebugger.XSourcePosition
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrDebuggerTypesHelper
import com.jetbrains.cidr.execution.debugger.evaluation.CidrMemberValue
import org.jetbrains.fortran.lang.psi.FortranCodeFragmentFactory
import org.jetbrains.fortran.lang.psi.FortranCompositeElement
import org.jetbrains.fortran.lang.psi.FortranFile
import org.jetbrains.fortran.lang.psi.ext.getNextNonCommentSibling
import org.jetbrains.fortran.lang.psi.ext.parentOfType

class FortranDebuggerTypesHelper(process: CidrDebugProcess) : CidrDebuggerTypesHelper(process) {
    override fun computeSourcePosition(value: CidrMemberValue): XSourcePosition? = null

    override fun isImplicitContextVariable(position: XSourcePosition, `var`: LLValue): Boolean? = false

    override fun resolveProperty(value: CidrMemberValue, dynamicTypeName: String?): XSourcePosition? = null

    override fun resolveToDeclaration(position: XSourcePosition, variable: LLValue): PsiElement? {
        if (!isFortran(position)) return delegate?.resolveToDeclaration(position, variable)

        val context = getContextElement(position)
        return resolveToDeclaration(context, variable.name)
    }

    private val delegate: CidrDebuggerTypesHelper? =
            FortranDebuggerLanguageSupportFactory.DELEGATE?.createTypesHelper(process)

    private fun isFortran(position: XSourcePosition): Boolean =
            getContextElement(position).containingFile is FortranFile
}

private fun resolveToDeclaration(ctx: PsiElement?, name: String): PsiElement? {
   /* val composite = ctx?.getNextNonCommentSibling()?.parentOfType<FortranCompositeElement>(strict = false)
            ?: return null
    val path = FortranCodeFragmentFactory(composite.project).createPath(name, composite)
            ?: return null*/

    return ctx//path.reference.resolve()
}