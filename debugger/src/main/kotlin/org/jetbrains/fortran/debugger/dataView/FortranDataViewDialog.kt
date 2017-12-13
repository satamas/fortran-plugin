package org.jetbrains.fortran.debugger.dataView

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBCheckBox
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.containerview.PyDataView
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess
import javax.swing.Action
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JPanel

class FortranDataViewDialog internal constructor(project: Project, value: CidrValue) : DialogWrapper(project, false) {
    private val myMainPanel: JPanel

    init {
        isModal = false
        setCancelButtonText("Close")
        setCrossClosesWindow(true)
        myMainPanel = JPanel(VerticalFlowLayout())
        val panel = FortranDataViewerPanel(project, value.process as FortranDebugProcess)
        panel.apply(value)
        myMainPanel.add(panel)
        panel.border = BorderFactory.createLineBorder(JBColor.GRAY)
        val colored = JBCheckBox("Colored cells")
        val resize = JBCheckBox("Resize Automatically")
        resize.isSelected = PropertiesComponent.getInstance(project).getBoolean(PyDataView.AUTO_RESIZE, true)
        colored.isSelected = PropertiesComponent.getInstance(project).getBoolean(PyDataView.COLORED_BY_DEFAULT, true)
        colored.addActionListener {
            panel.isColored = colored.isSelected
            panel.updateUI()
        }
        resize.addActionListener {
            panel.resize(resize.isSelected)
            panel.updateUI()
        }
        myMainPanel.add(colored)
        myMainPanel.add(resize)
        title = value.name
        init()
    }

    override fun createActions(): Array<Action> {
        return arrayOf(cancelAction)
    }

    override fun createCenterPanel(): JComponent? {
        return myMainPanel
    }
}
