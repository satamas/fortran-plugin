package org.jetbrains.fortran.debugger.dataView

import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.python.debugger.dataview.DataViewValueHolder
import com.jetbrains.python.debugger.dataview.DataViewValueHolderProvider
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess


class FortranDataViewValueHolderProvider : DataViewValueHolderProvider() {
    override fun createHolder(value: XNamedValue): DataViewValueHolder? {
        return if (value is CidrPhysicalValue && value.process is FortranDebugProcess) {
            FortranDataViewValueHolder(value as CidrPhysicalValue)
        } else null
    }
}
