package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.cidr.cpp.execution.CLionRunParameters
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration
import com.jetbrains.cidr.cpp.execution.CMakeLauncher
import com.jetbrains.cidr.cpp.execution.debugger.backend.GDBDriverConfiguration
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess


class FortranLauncher(configuration : CMakeAppRunConfiguration) : CMakeLauncher(configuration) {
    @Throws(ExecutionException::class)
    override fun createDebugProcess(state: CommandLineState, session: XDebugSession): CidrDebugProcess {
        val cidrDebugProcess = super.createDebugProcess(state, session)
        val configuration = FortranGDBDriverProxy(
                (cidrDebugProcess.runParameters as CLionRunParameters).debuggerDriverConfiguration as GDBDriverConfiguration
        )
        val installer = (cidrDebugProcess.runParameters as CLionRunParameters).installer
        return FortranDebugProcess(CLionRunParameters(configuration, installer), cidrDebugProcess.session, state.consoleBuilder)
    }
}