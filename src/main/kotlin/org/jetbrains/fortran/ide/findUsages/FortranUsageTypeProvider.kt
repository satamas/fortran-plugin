package org.jetbrains.fortran.ide.findUsages

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import org.jetbrains.fortran.lang.psi.*

object FortranUsageTypeProvider : UsageTypeProvider {
    override fun  getUsageType(element : PsiElement? ) : UsageType? {
        if (element == null) return null
        return when (element) {
            is FortranLabel -> UsageType("Fortran label")
            is FortranConstructName -> UsageType("Construct name")
            is FortranTypeName -> UsageType("Type")
            is FortranDataPath -> UsageType("Entity")
            is FortranCommonBlockDecl -> UsageType("Common block")
            else -> null
        }
    }
}