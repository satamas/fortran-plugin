package org.jetbrains.fortran.debugger.dataView

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.DataViewAccessor
import com.jetbrains.python.debugger.containerview.*
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess

class FortranDataView(project: Project) : DataView(project) {

    override fun show(value: XNamedValue, inToolwindow: Boolean) {
        val fortranValue = value as CidrValue
        if (inToolwindow) {
            showInToolwindow(fortranValue)
        } else {
            ApplicationManager.getApplication().invokeLater {
                val dialog = FortranDataViewDialog(myProject)
                dialog.init(myProject, fortranValue)
                dialog.show()
            }
        }
    }

    override fun showInToolwindow(value: XNamedValue) {
        val fortranValue = value as CidrValue
        val window = ToolWindowManager.getInstance(myProject).getToolWindow(DataView.DATA_VIEWER_ID)
        if (window == null) {
            DataView.LOG.error("Tool window '" + DataView.DATA_VIEWER_ID + "' is not found")
            return
        }
        window.contentManager.getReady(this).doWhenDone {
            val selectedInfo = addTab(fortranValue.process as FortranDebugProcess)
            val dataViewerPanel = selectedInfo.component as PyDataViewerPanel
            dataViewerPanel.apply(fortranValue)
        }
        window.show(null)
        val dataView = window.contentManager.getContent(0)
        if (dataView != null) {
            window.contentManager.setSelectedContent(dataView)
        }
    }


    override fun createNewPanel(project: Project, dataViewAccessor: DataViewAccessor): DataViewerPanel {
        throw NotImplementedError("createNewPanel")
        //return FortranDataViewerPanel(project, dataViewAccessor as FortranDebugProcess)
    }

    fun getInstance(project: Project): PyDataView {
        return ServiceManager.getService(project, PyDataView::class.java)
    }

    companion object {
        fun getInstance(project: Project): FortranDataView =
                ServiceManager.getService(project, FortranDataView::class.java)
    }
}