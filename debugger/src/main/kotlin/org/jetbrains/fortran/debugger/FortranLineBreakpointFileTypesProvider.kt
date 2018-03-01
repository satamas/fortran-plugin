package org.jetbrains.fortran.debugger

import com.jetbrains.cidr.execution.debugger.breakpoints.CidrLineBreakpointFileTypesProvider
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType

class FortranLineBreakpointFileTypesProvider : CidrLineBreakpointFileTypesProvider {
    override fun getFileTypes() = setOf(FortranFileType, FortranFixedFormFileType)
}