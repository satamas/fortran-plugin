package org.jetbrains.fortran.debugger.lang

import com.jetbrains.cidr.execution.debugger.CidrDebuggerEditorsExtensionBase
import org.jetbrains.fortran.FortranLanguage

class FortranDebuggerEditorsProvider : CidrDebuggerEditorsExtensionBase() {
    override fun getSupportedLanguage() = FortranLanguage
}
