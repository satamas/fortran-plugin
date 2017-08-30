package org.jetbrains.fortran.ide.findUsages

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import org.jetbrains.fortran.lang.psi.FortranConstructName
import org.jetbrains.fortran.lang.psi.FortranDataPath
import org.jetbrains.fortran.lang.psi.FortranLabel
import org.jetbrains.fortran.lang.psi.FortranTypeName

object FortranUsageTypeProvider : UsageTypeProvider {
    override fun  getUsageType(element : PsiElement? ) : UsageType? {
        if (element == null) return null
        return when (element) {
            is FortranLabel -> UsageType("Fortran label")
            is FortranConstructName -> UsageType("Construct name")
            is FortranTypeName -> UsageType("Type")
            is FortranDataPath -> UsageType("Entity")
            else -> null
        }
    }
}