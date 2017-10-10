package org.jetbrains.fortran.debugger.dataView

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.impl.ui.tree.XDebuggerTree
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import javax.swing.tree.TreePath


class FortranViewNumericContainerAction : XDebuggerTreeActionBase() {
    override fun perform(node: XValueNodeImpl?, nodeName: String, e: AnActionEvent) {
        val p = e.project
        if (p != null && node != null && node.valueContainer is CidrValue) {
            val debugValue = node.valueContainer as CidrValue
            showFortranNumericViewer(p, debugValue)
        }
    }

    private fun getSelectedPaths(dataContext: DataContext): Array<TreePath>? {
        val tree = XDebuggerTree.getTree(dataContext)
        return tree?.selectionPaths
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = false
        val paths = getSelectedPaths(e.dataContext)
        if (paths != null) {
            if (paths.size > 1) {
                e.presentation.isVisible = false
                return
            }

            val node = XDebuggerTreeActionBase.getSelectedNode(e.dataContext)
            if (node != null && node.valueContainer is CidrValue) {
                e.presentation.isVisible = true
            } else {
                e.presentation.isVisible = false
            }
        }
    }

    fun showFortranNumericViewer(project : Project, debugValue : CidrValue) {
        FortranDataView.get(project).showFortranValue(debugValue)
  }
}