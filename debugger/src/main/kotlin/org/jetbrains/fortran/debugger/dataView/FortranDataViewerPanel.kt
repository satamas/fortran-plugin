package org.jetbrains.fortran.debugger.dataView

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorTextField
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.PyDebugValue
import com.jetbrains.python.debugger.PyDebuggerException
import com.jetbrains.python.debugger.PyFrameAccessor
import com.jetbrains.python.debugger.array.AsyncArrayTableModel
import com.jetbrains.python.debugger.array.JBTableWithRowHeaders
import com.jetbrains.python.debugger.containerview.DataView
import com.jetbrains.python.debugger.containerview.DataViewStrategy
import com.jetbrains.python.debugger.containerview.DataViewerPanel
import org.jetbrains.fortran.FortranFileType
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class FortranDataViewerPanel(project: Project, frameAccessor: FortranDebugProcess)
    : DataViewerPanel(project, frameAccessor) {

    override fun updateDebugValue(model: AsyncArrayTableModel) {
        val oldValue = model.debugValue
        if (!oldValue.isTemporary) {
            return
        }
        val newValue = getDebugValue(mySliceTextField.getText()) as PyDebugValue
        if (newValue != null) {
            model.debugValue = newValue
        }
    }

    override fun getFrameAccessor(): PyFrameAccessor {
        return myFrameAccessor as PyFrameAccessor
    }

    protected override fun createUIComponents() {
        myFormatTextField = createEditorField()
        mySliceTextField = createEditorField()
        addCompletion()

        myTable = JBTableWithRowHeaders(PropertiesComponent.getInstance(myProject).getBoolean(DataView.AUTO_RESIZE, true))
        myScrollPane = myTable.getScrollPane()
    }

    private fun addCompletion() {
        throw NotImplementedError("addCompletion")
    }

    private fun createEditorField(): EditorTextField {
        return object : EditorTextField(EditorFactory.getInstance().createDocument(""), myProject, FortranFileType, false, true) {
            override fun createEditor(): EditorEx {
                val editor = super.createEditor()
                editor.contentComponent.addKeyListener(object : KeyAdapter() {
                    override fun keyPressed(e: KeyEvent?) {
                        if (e!!.keyCode == KeyEvent.VK_ENTER) {
                            apply(mySliceTextField.getText())
                        }
                    }
                })
                return editor
            }
        }
    }

    override fun apply(name: String) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val debugValue = getDebugValue(name) as? CidrValue
            if (debugValue != null) {
                ApplicationManager.getApplication().invokeLater { apply(debugValue) }
            }
        }
    }

    override fun apply(debugValue: XNamedValue) {
        myErrorLabel.setVisible(false)
        val pyDebugValue = debugValue as PyDebugValue
        val type = pyDebugValue.type
        val strategy = DataViewStrategy.getStrategy(type)
        if (strategy == null) {
            setError(type!! + " is not supported")
            return
        }
        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                val arrayChunk = pyDebugValue.frameAccessor.getArrayItems(pyDebugValue, 0, 0, -1, -1, getFormat())
                ApplicationManager.getApplication().invokeLater { updateUI(arrayChunk, pyDebugValue, strategy) }
            } catch (e: PyDebuggerException) {
                DataViewerPanel.LOG.error(e)
            }
        }
    }

    override fun resize(autoResize: Boolean) {
        myTable.setAutoResize(autoResize)
        apply(getSliceTextField().getText())
    }

    private fun updateUI(chunk: ArrayChunk, debugValue: PyDebugValue, strategy: DataViewStrategy) {
        throw NotImplementedError("updateUI")
    }

    override fun checkErrorOnEval(value: XNamedValue?): Boolean {
        if ((value as PyDebugValue).isErrorOnEval) {
            setError(value.value)
            return false
        }
        return true
    }
}