package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.jetbrains.cidr.execution.RunParameters
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrBreakpointHandler
import org.jetbrains.fortran.debugger.FortranLineBreakpointType

class FortranDebugProcess(parameters: RunParameters, session: XDebugSession, consoleBuilder: TextConsoleBuilder)
    : CidrLocalDebugProcess(parameters, session, consoleBuilder) {

    private val fortranBreakPointHandler = createFortranBreakpointHandler()

    private fun createFortranBreakpointHandler(): CidrBreakpointHandler {
        return CidrBreakpointHandler(this, FortranLineBreakpointType::class.java)
    }

    override fun getBreakpointHandlers(): Array<XBreakpointHandler<*>> {
        return super.getBreakpointHandlers() + fortranBreakPointHandler
    }

    override fun handleBreakpoint(stopPlace: DebuggerDriver.StopPlace, breakpointNumber: Int) {
        val b: XBreakpoint<*>? = fortranBreakPointHandler.getCodepoint(breakpointNumber)
        if (b != null) {
            handleCodepoint(stopPlace, b)
        } else {
            super.handleBreakpoint(stopPlace, breakpointNumber)
        }
    }
}
