package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebugSessionListener
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.impl.XDebugProcessConfiguratorStarter
import com.intellij.xdebugger.impl.ui.XDebugSessionData
import com.jetbrains.cidr.cpp.execution.CMakeLauncher
import com.jetbrains.cidr.execution.CidrCommandLineState
import com.jetbrains.cidr.execution.CidrRunProfile
import com.jetbrains.cidr.execution.CidrRunner
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.FortranFixedFormFileType

class FortranRunner : CidrRunner() {
    override fun getRunnerId() = "FortranRunner"

    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        if (!(DefaultRunExecutor.EXECUTOR_ID == executorId || DefaultDebugExecutor.EXECUTOR_ID == executorId)) return false

        return (profile is CidrRunProfile)
    }

    override fun doExecute(state: RunProfileState, env: ExecutionEnvironment): RunContentDescriptor? {
        CidrRunner.triggerUsage(env.runnerAndConfigurationSettings)
        if (DefaultDebugExecutor.EXECUTOR_ID == env.executor.id) {
            return startDebugSession(state as CidrCommandLineState, env, false).runContentDescriptor
        }
        return super.doExecute(state, env)

    }

    @Throws(ExecutionException::class)
    override fun startDebugSession(state: CidrCommandLineState,
                          env: ExecutionEnvironment,
                          muteBreakpoints: Boolean,
                          vararg listeners: XDebugSessionListener): XDebugSession {
        return XDebuggerManager.getInstance(env.project).startSession(env, object : XDebugProcessConfiguratorStarter() {
            @Throws(ExecutionException::class)
            override fun start(session: XDebugSession): XDebugProcess {
                for (l in listeners) {
                    session.addSessionListener(l)
                }
                val defaultDebugProcess : XDebugProcess = state.startDebugProcess(session)


                val fortranFiles = FileTypeIndex.getFiles(FortranFileType, GlobalSearchScope.projectScope(session.project))
                        .plus(FileTypeIndex.getFiles(FortranFixedFormFileType, GlobalSearchScope.projectScope(session.project)))
                if (defaultDebugProcess is CidrLocalDebugProcess && state.launcher is CMakeLauncher
                        && !fortranFiles.isEmpty()) {

                    defaultDebugProcess.stop()
                    val newProc = FortranDebugProcess((defaultDebugProcess).runParameters, session, state.consoleBuilder)
                    newProc.start()
                    return newProc
                }
                return defaultDebugProcess
            }

            override fun configure(data: XDebugSessionData) {
                if (muteBreakpoints) {
                    data.isBreakpointsMuted = true
                }
            }
        })
    }
}