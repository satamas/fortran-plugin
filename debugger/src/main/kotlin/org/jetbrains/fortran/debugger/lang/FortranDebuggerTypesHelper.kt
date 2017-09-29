package org.jetbrains.fortran.debugger.lang

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.xdebugger.XSourcePosition
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.LLValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrDebuggerTypesHelper
import com.jetbrains.cidr.execution.debugger.evaluation.CidrMemberValue
import org.jetbrains.fortran.lang.psi.*
import org.jetbrains.fortran.lang.psi.ext.getNextNonCommentSibling
import org.jetbrains.fortran.lang.psi.ext.parentOfType

class FortranDebuggerTypesHelper(process: CidrDebugProcess) : CidrDebuggerTypesHelper(process) {
    override fun computeSourcePosition(value: CidrMemberValue): XSourcePosition? = null

    override fun isImplicitContextVariable(position: XSourcePosition, `var`: LLValue): Boolean? = false

    override fun resolveProperty(value: CidrMemberValue, dynamicTypeName: String?): XSourcePosition? = null

    override fun resolveToDeclaration(position: XSourcePosition, variable: LLValue): PsiElement? {
        if (!isFortran(position)) return delegate?.resolveToDeclaration(position, variable)

        val context = getContextElement(position)
        val programUnit = PsiTreeUtil.getParentOfType(context, FortranProgramUnit::class.java)
        val declarations = programUnit?.variables?.filter { it.name.equals(variable.name, true) } ?: emptyList()
        if (declarations.firstOrNull() != null) return declarations.firstOrNull()
        val outerProgramUnit = PsiTreeUtil.getParentOfType(programUnit, FortranProgramUnit::class.java)
        val outerDeclarations = outerProgramUnit?.variables?.filter { it.name.equals(variable.name, true) } ?: emptyList()
        return outerDeclarations.firstOrNull()
    }

    private val delegate: CidrDebuggerTypesHelper? =
            FortranDebuggerLanguageSupportFactory.DELEGATE?.createTypesHelper(process)

    private fun isFortran(position: XSourcePosition): Boolean =
            getContextElement(position).containingFile is FortranFile || getContextElement(position).containingFile is FortranFixedFormFile
}
