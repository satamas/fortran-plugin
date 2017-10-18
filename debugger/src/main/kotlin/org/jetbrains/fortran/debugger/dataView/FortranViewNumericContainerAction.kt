package org.jetbrains.fortran.debugger.dataView

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.util.PlatformUtils
import com.intellij.xdebugger.impl.ui.tree.XDebuggerTree
import com.intellij.xdebugger.impl.ui.tree.actions.XDebuggerTreeActionBase
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue


class FortranViewNumericContainerAction : XDebuggerTreeActionBase() {

    override fun perform(node : XValueNodeImpl?, nodeName : String , e : AnActionEvent) {
        val p = e.project
        if (p != null && node != null && node.valueContainer is CidrPhysicalValue && node.isComputed) {
            val debugValue = node.valueContainer as CidrPhysicalValue
            showNumericViewer(p, debugValue);
        }
    }

    private fun showNumericViewer(project: Project, debugValue : CidrPhysicalValue) {
        FortranDataView.getInstance(project).show(debugValue, PlatformUtils.isPyCharmPro());
    }

    companion object {
        fun getSelectedPaths(dataContext : DataContext) = XDebuggerTree.getTree(dataContext)?.selectionPaths
    }


    override fun update(e : AnActionEvent) {
        e.presentation.isVisible = false
        val paths = getSelectedPaths(e.dataContext)
        if (paths != null) {
            if (paths.size > 1) {
                e.presentation.isVisible = false
                return;
            }

            val node = getSelectedNode(e.dataContext)
            if (node != null && node.valueContainer is CidrPhysicalValue && node.isComputed) {
                val debugValue = node.valueContainer as CidrPhysicalValue

           //     val nodeType = debugValue.typesHelper.
           //     println(nodeType)
          //      println( debugValue.presentationVar.typeClass?.toString())
                if (true) {
                    e.presentation.text = "View as Array";
                    e.presentation.isVisible = true;
                }
            }
            else
            {
                e.presentation.isVisible = false
            }
        }
    }
}