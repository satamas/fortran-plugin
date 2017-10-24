package org.jetbrains.fortran.debugger.dataView

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.tabs.TabInfo
import com.intellij.xdebugger.XDebugProcess
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.python.debugger.containerview.DataView
import com.jetbrains.python.debugger.containerview.DataViewDialog
import com.jetbrains.python.debugger.containerview.DataViewerPanel
import com.jetbrains.python.debugger.dataview.DataViewFrameAccessor
import com.jetbrains.python.debugger.dataview.DataViewValueHolderFactory
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess

class FortranDataView(project: Project) : DataView(project) {

    fun show(value: CidrPhysicalValue, inToolwindow: Boolean) {
        if (inToolwindow) {
            showInToolwindow(value)
        } else {
            ApplicationManager.getApplication().invokeLater({
                val dialog = DataViewDialog(myProject, DataViewValueHolderFactory.createHolder(value))
                dialog.show()
            })
        }
    }

    private fun showInToolwindow(value: CidrPhysicalValue) {
        val window = ToolWindowManager.getInstance(myProject).getToolWindow(DataView.DATA_VIEWER_ID)
        if (window == null) {
            DataView.LOG.error("Tool window '" + DataView.DATA_VIEWER_ID + "' is not found")
            return
        }
        window.contentManager.getReady(this).doWhenDone({
            val selectedInfo = addTab(value.process as FortranDebugProcess)
            val dataViewerPanel = selectedInfo.component as DataViewerPanel
            dataViewerPanel.apply(DataViewValueHolderFactory.createHolder(value))
        })
        window.show(null)
        val dataView = window.contentManager.getContent(0)
        if (dataView != null) {
            window.contentManager.setSelectedContent(dataView)
        }
    }

    override fun addTab(frameAccessor: DataViewFrameAccessor): TabInfo {
        if (hasOnlyEmptyTab()) {
            myTabs.removeTab(myTabs.selectedInfo)
        }
        val panel = DataViewerPanel(myProject, frameAccessor)
        val info = TabInfo(panel)

        if (frameAccessor is XDebugProcess) {
            info.icon = AllIcons.Toolwindows.ToolWindowDebugger
            val name = (frameAccessor as XDebugProcess).session.sessionName
            info.tooltipText = "Connected to debug session '$name'"
        }
        info.text = DataView.EMPTY_TAB_NAME
        info.preferredFocusableComponent = panel.sliceTextField
        info.setActions(DefaultActionGroup(NewViewerAction(frameAccessor)), ActionPlaces.UNKNOWN)
        info.setTabLabelActions(DefaultActionGroup(CloseViewerAction(info, frameAccessor)), ActionPlaces.UNKNOWN)
        panel.addListener({ name -> info.text = name })
        myTabs.addTab(info)
        myTabs.select(info, true)
        return info
    }

    companion object {
        fun getInstance(project: Project): FortranDataView =
                ServiceManager.getService(project, FortranDataView::class.java)
    }
}
