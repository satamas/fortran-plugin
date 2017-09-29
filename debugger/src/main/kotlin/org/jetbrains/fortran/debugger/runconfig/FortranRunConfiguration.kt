package org.jetbrains.fortran.debugger.runconfig


import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration
import com.jetbrains.cidr.execution.CidrCommandLineState

class FortranRunConfiguration constructor( project:Project,
                                                     factory : ConfigurationFactory , name : String )
    : CMakeAppRunConfiguration(project, factory, name) {

    @Throws(ExecutionException::class)
    override fun getState(executor: Executor, env: ExecutionEnvironment): CidrCommandLineState? {
        return CidrCommandLineState(env, FortranLauncher(this))
    }

}
