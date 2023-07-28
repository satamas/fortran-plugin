package org.jetbrains.fortran.ide.findUsages

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import org.jetbrains.fortran.lang.psi.*

class FortranUsageTypeProvider : UsageTypeProvider {
    override fun getUsageType(element: PsiElement): UsageType? {
        return when (element) {
            is FortranLabel -> UsageType { "Fortran label" }
            is FortranConstructName -> UsageType { "Construct name" }
            is FortranTypeName -> UsageType { "Type" }
            is FortranDataPath -> UsageType { "Entity" }
            is FortranCommonBlockDecl -> UsageType { "Common block" }
            is FortranUnit -> UsageType { "Fortran unit" }
            else -> null
        }
    }
}