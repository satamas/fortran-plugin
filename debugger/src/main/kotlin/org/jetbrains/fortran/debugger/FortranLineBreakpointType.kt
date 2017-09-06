package org.jetbrains.fortran.debugger

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrLineBreakpointType
import org.jetbrains.fortran.FortranFileType

class FortranLineBreakpointType : CidrLineBreakpointType(
        //    "org.jetbrains.fortran.debugger.FortranLineBreakpointType",
//    CidrDebuggerBundle.message("debug.breakpoint.line")
) {
    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean =
            file.fileType == FortranFileType
}