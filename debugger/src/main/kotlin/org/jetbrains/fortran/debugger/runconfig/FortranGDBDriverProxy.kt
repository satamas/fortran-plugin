package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.Expirable
import com.intellij.openapi.util.io.FileUtil
import com.jetbrains.cidr.cpp.execution.debugger.backend.GDBDriverConfiguration
import com.jetbrains.cidr.execution.Installer
import com.jetbrains.cidr.execution.debugger.CidrStackFrame
import com.jetbrains.cidr.execution.debugger.backend.*
import com.jetbrains.cidr.execution.debugger.backend.gdb.GDBDriver
import com.jetbrains.cidr.execution.debugger.evaluation.EvaluationContext
import java.io.File


class FortranGDBDriverProxy(val configuration : GDBDriverConfiguration) : DebuggerDriverConfiguration() {
    override fun getDriverName(): String {
        return configuration.driverName
    }

    override fun isAttachSupported(): Boolean {
        return configuration.isAttachSupported
    }

    @Throws(ExecutionException::class)
    override fun createDriver(handler: DebuggerDriver.Handler): DebuggerDriver {
        return GDBDriver(handler, this)
    }

    @Throws(ExecutionException::class)
    override fun createDriverCommandLine(driver: DebuggerDriver, installer : Installer): GeneralCommandLine {
        val standardCommandLine = configuration.createDriverCommandLine(driver, installer)
        standardCommandLine.putUserData(GDBDriver.PRETTY_PRINTERS_PATH,
            FileUtil.toSystemIndependentName(getFortranPrettyPrinters().absolutePath))
        return standardCommandLine
    }

    override fun convertToLocalPath(absolutePath: String?): String? {
        return configuration.convertToLocalPath(absolutePath)
    }

    override fun convertToEnvPath(localPath: String?): String? {
        return configuration.convertToEnvPath(localPath)
    }

    override fun createEvaluationContext(driver: DebuggerDriver,
                                         expirable: Expirable?,
                                         frame: CidrStackFrame): EvaluationContext {
        return configuration.createEvaluationContext(driver, expirable, frame)
    }

    companion object {
        fun getFortranPrettyPrinters(): File {
            return File(PathManager.getConfigPath()+"/plugins/fortran-plugin/lib/gdb/renderers")
        }
    }
}