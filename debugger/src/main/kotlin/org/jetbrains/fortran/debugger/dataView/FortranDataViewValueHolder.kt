package org.jetbrains.fortran.debugger.dataView

import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.python.debugger.dataview.DataViewValueHolder
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess

class FortranDataViewValueHolder(private val cidrValue: CidrPhysicalValue) : DataViewValueHolder {
    override val value: CidrPhysicalValue
        get() = cidrValue

    override fun isTemporary(): Boolean = false
    override fun isErrorOnEval(): Boolean = false
    override fun getType(): String? = cidrValue.type
    override fun getName(): String = cidrValue.name
    override fun getFrameAccessor(): FortranDebugProcess = cidrValue.process as FortranDebugProcess
    override fun getTempName(): String? = cidrValue.name
    override fun getValueString() : String? = cidrValue.preparedRenderer.toString()
    override fun getFullName(): String = cidrValue.name

}
