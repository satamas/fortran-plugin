package org.jetbrains.fortran.lang.resolve.ref

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import org.jetbrains.fortran.lang.psi.FortranConstructLabel
import org.jetbrains.fortran.lang.psi.FortranNumericalLabel

object FortranUsageTypeProvider : UsageTypeProvider {
    override fun  getUsageType(element : PsiElement? ) : UsageType? {
        if (element == null) return null
        if (element is FortranNumericalLabel) {
            return UsageType("Fortran label")
        }
        if (element is FortranConstructLabel) {
            return UsageType("Construct name")
        }
        return null
    }
}