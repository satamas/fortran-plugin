package org.jetbrains.fortran.debugger

import com.intellij.openapi.fileTypes.FileType
import com.intellij.util.containers.ContainerUtil
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrLineBreakpointFileTypesProvider
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType

class FortranLineBreakpointFileTypesProvider : CidrLineBreakpointFileTypesProvider {

    override fun getFileTypes(): Set<FileType> {
        return TYPES
    }

    companion object {
        private val TYPES = ContainerUtil.immutableSet<FileType>(FortranFileType, FortranFixedFormFileType)
    }
}