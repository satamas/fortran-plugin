package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.cidr.cpp.execution.CLionRunParameters
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration
import com.jetbrains.cidr.cpp.execution.CMakeLauncher
import com.jetbrains.cidr.cpp.execution.debugger.backend.GDBDriverConfiguration
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess

class FortranLauncher(environment : ExecutionEnvironment, configuration : CMakeAppRunConfiguration) : CMakeLauncher(environment, configuration) {
    @Throws(ExecutionException::class)
    override fun createDebugProcess(state: CommandLineState, session: XDebugSession): CidrDebugProcess {
        val cidrDebugProcess = super.createDebugProcess(state, session)
        val driverConfiguration = (cidrDebugProcess.runParameters as CLionRunParameters).debuggerDriverConfiguration as? GDBDriverConfiguration
            ?: throw ExecutionException("Fortran debugger only works with GDB. LLDB is not supported. Please, select GDB in Toolchains -> Debugger.")
        val configuration = FortranGDBDriverProxy(driverConfiguration)
        val installer = (cidrDebugProcess.runParameters as CLionRunParameters).installer
        return CidrLocalDebugProcess(CLionRunParameters(configuration, installer), cidrDebugProcess.session, state.consoleBuilder)
    }
}