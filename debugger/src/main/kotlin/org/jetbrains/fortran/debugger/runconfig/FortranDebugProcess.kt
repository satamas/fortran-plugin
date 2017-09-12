package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.cidr.execution.RunParameters
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrBreakpointHandler
import org.jetbrains.fortran.debugger.FortranLineBreakpointType

class FortranDebugProcess @Throws(ExecutionException::class)
constructor(parameters: RunParameters, session: XDebugSession, consoleBuilder: TextConsoleBuilder)
    : CidrLocalDebugProcess(parameters, session, consoleBuilder) {

    override fun createBreakpointHandler(): CidrBreakpointHandler {
        return CidrBreakpointHandler(this, FortranLineBreakpointType::class.java)
    }
}
