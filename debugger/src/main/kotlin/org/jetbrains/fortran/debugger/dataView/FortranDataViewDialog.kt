package org.jetbrains.fortran.debugger.dataView

import com.intellij.openapi.project.Project
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.containerview.DataViewDialog
import com.jetbrains.python.debugger.containerview.DataViewerPanel
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess


class FortranDataViewDialog internal constructor(project: Project) : DataViewDialog(project) {
    override fun createDataViewerPanel(project: Project, value: XNamedValue): DataViewerPanel {
        return FortranDataViewerPanel(project, (value as CidrValue).process as FortranDebugProcess)
    }

    override fun createTitle(value: XNamedValue): String {
        return (value as CidrValue).name
    }
}
