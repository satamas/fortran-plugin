package org.jetbrains.fortran.debugger.dataView

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.PyDebugValue
import com.jetbrains.python.debugger.containerview.PyDataView

class FortranDataView(project: Project) : PyDataView(project) {

    fun showFortranValue(value: CidrValue) {
        val pyVal = PyDebugValue(value.name, null, null, null, true, false, false, false, null)

        super.show(pyVal)
    }

    companion object {
        fun get(project: Project): FortranDataView =
                ServiceManager.getService(project, FortranDataView::class.java)
    }
}