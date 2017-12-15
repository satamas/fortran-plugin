package org.jetbrains.fortran.debugger.dataView

import com.intellij.openapi.project.Project
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.DataViewAccessor
import com.jetbrains.python.debugger.containerview.DataViewStrategy
import com.jetbrains.python.debugger.containerview.DataViewerPanel
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess


class FortranDataViewerPanel(project: Project, frameAccessor: FortranDebugProcess)
    : DataViewerPanel(project, frameAccessor) {

    override fun getFrameAccessor(): FortranDebugProcess {
        return myFrameAccessor as FortranDebugProcess
    }

    override fun getFrameAccessor(value: XNamedValue): DataViewAccessor {
        return (value as CidrValue).process as FortranDebugProcess
    }

    override fun getStrategy(debugValue: XNamedValue?): DataViewStrategy? {
        return FortranArrayViewStrategy()
    }

    override fun getDebugValueName(debugValue: XNamedValue, chunk: ArrayChunk): String {
        return (debugValue as CidrValue).name
    }
}