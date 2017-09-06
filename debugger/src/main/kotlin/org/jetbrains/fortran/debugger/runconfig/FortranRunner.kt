package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.jetbrains.cidr.execution.CidrCommandLineState
import com.jetbrains.cidr.execution.CidrRunner

class FortranRunner : CidrRunner() {
    override fun getRunnerId() = "FortranRunner"

    override fun canRun(executorId: String, profile: RunProfile): Boolean {
        if (!(DefaultRunExecutor.EXECUTOR_ID == executorId || DefaultDebugExecutor.EXECUTOR_ID == executorId)) return false
        return super.canRun(executorId, profile)
    }

    override fun doExecute(state: RunProfileState, env: ExecutionEnvironment): RunContentDescriptor? {
        CidrRunner.triggerUsage(env.runnerAndConfigurationSettings)
        if (DefaultDebugExecutor.EXECUTOR_ID == env.executor.id) {
            return startDebugSession(state as CidrCommandLineState, env, false).runContentDescriptor
        }
        return super.doExecute(state, env)
    }
}