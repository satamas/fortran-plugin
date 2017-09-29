package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration
import com.jetbrains.cidr.cpp.execution.CMakeLauncher
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess


class FortranLauncher(configuration : CMakeAppRunConfiguration) : CMakeLauncher(configuration) {
    @Throws(ExecutionException::class)
    override fun createDebugProcess(state: CommandLineState, session: XDebugSession): CidrDebugProcess {
        val cidrDebugProcess = super.createDebugProcess(state, session)
        return FortranDebugProcess(cidrDebugProcess.runParameters, cidrDebugProcess.session, state.consoleBuilder)
    }
}