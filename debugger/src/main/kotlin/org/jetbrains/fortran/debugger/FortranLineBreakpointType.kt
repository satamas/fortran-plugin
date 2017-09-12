package org.jetbrains.fortran.debugger

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrLineBreakpointType
import com.jetbrains.cidr.lang.OCFileType
import com.jetbrains.cidr.lang.asm.AsmFileType
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType

class FortranLineBreakpointType : CidrLineBreakpointType(
        "org.jetbrains.fortran.debugger.FortranLineBreakpointType",
        "debug.breakpoint.line"
) {
    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean =
            file.fileType == FortranFileType     || file.fileType == FortranFixedFormFileType
         || file.fileType == OCFileType.INSTANCE || file.fileType == AsmFileType.INSTANCE
}