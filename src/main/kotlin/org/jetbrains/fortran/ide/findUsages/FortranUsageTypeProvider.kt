package org.jetbrains.fortran.ide.findUsages

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import org.jetbrains.fortran.lang.psi.FortranConstructName
import org.jetbrains.fortran.lang.psi.FortranLabel

object FortranUsageTypeProvider : UsageTypeProvider {
    override fun  getUsageType(element : PsiElement? ) : UsageType? {
        if (element == null) return null
        if (element is FortranLabel) {
            return UsageType("Fortran label")
        }
        if (element is FortranConstructName) {
            return UsageType("Construct name")
        }
        return null
    }
}