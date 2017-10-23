package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.containers.ContainerUtil
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebugSessionListener
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XDebuggerTreeNodeHyperlink
import com.intellij.xdebugger.frame.XValueChildrenList
import com.jetbrains.cidr.execution.RunParameters
import com.jetbrains.cidr.execution.debugger.*
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrBreakpointHandler
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.cidr.execution.debugger.evaluation.EvaluationContext
import com.jetbrains.python.debugger.*
import com.jetbrains.python.debugger.dataview.DataViewFrameAccessor
import com.jetbrains.python.debugger.dataview.DataViewValueHolder
import org.jetbrains.fortran.debugger.FortranLineBreakpointType
import org.jetbrains.fortran.debugger.dataView.FortranViewNumericContainerAction
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import javax.swing.Icon

class FortranDebugProcess(parameters: RunParameters, session: XDebugSession, consoleBuilder: TextConsoleBuilder)
    : CidrLocalDebugProcess(parameters, session, consoleBuilder)
    , DataViewFrameAccessor {
    init {
        session.addSessionListener(object : XDebugSessionListener {
            override fun stackFrameChanged() {
                for (listener in myFrameListeners) {
                    listener.frameChanged()
                }
            }
        })
    }

    private val myFrameListeners = ContainerUtil.createLockFreeCopyOnWriteList<PyFrameListener>()
    private val fortranBreakPointHandler = createFortranBreakpointHandler()

    private fun createFortranBreakpointHandler(): CidrBreakpointHandler {
        return CidrBreakpointHandler(this, FortranLineBreakpointType::class.java)
    }

    override fun getBreakpointHandlers(): Array<XBreakpointHandler<*>> {
        return super.getBreakpointHandlers() + fortranBreakPointHandler
    }

    override fun handleBreakpoint(stopPlace: DebuggerDriver.StopPlace, breakpointNumber: Int) {
        val b: XBreakpoint<*>? = fortranBreakPointHandler.getCodepoint(breakpointNumber)
        if (b != null) {
            handleCodepoint(stopPlace, b)
        } else {
            super.handleBreakpoint(stopPlace, breakpointNumber)
        }
    }

    // for data view
    @Throws(PyDebuggerException::class)
    override fun evaluate(expression: String, execute: Boolean, doTrunc: Boolean): CidrValue {
        throw PyDebuggerException("AAAAAAAAAAAAAA")
    }


    @Throws(PyDebuggerException::class)
    override fun loadFrame(): XValueChildrenList? {
        throw PyDebuggerException("AAAAAAAAAAAAAA")
    }

    @Throws(PyDebuggerException::class)
    override fun getArrayItems(holder: DataViewValueHolder, rowOffset: Int, colOffset: Int, rows: Int, cols: Int, format: String): ArrayChunk
    {
        val result = ArrayChunkBuilder().setHolder(holder).setSlicePresentation(holder.getName())
        postCommand { driver ->
            val context = createEvaluationContext(driver, null, session.currentStackFrame as CidrStackFrame)
            val node = DataViewNode(context, result, holder.getName())
            (holder.value as CidrPhysicalValue).frame.computeChildren(node)
        }

        val semaphore = Semaphore(0)



        try {
                semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS)
        } catch (ignore: InterruptedException) {
        }

        val chunk = result.createArrayChunk()
        return chunk
    }


    override fun addFrameListener(listener: PyFrameListener) {
        myFrameListeners.add(listener)
    }



    private class DataViewNode(val context : EvaluationContext,
                               val builder: ArrayChunkBuilder,
                               val varName : String) : XCompositeNode {
        var is2D : Boolean = false
        var rowN : Int = 0
        val data = mutableListOf<MutableList<String>>()
        val rowNames = mutableListOf<String>()
        val colNames = mutableListOf<String>()

        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            for (i in 0 until children.size()) {
                if (children.getName(i).equals(varName, true)) {
                    children.getValue(i).computeChildren(this)
                    return

                }
            }

            // type
            val type = (children.getValue(0) as CidrPhysicalValue).type
            val arrayType = when {
                type.contains("integer") -> "i"
                type.contains("real") -> "f"
                type.contains("logical") -> "b"
                else -> "c"
            }
            builder.setType(arrayType)

            val in2D : Boolean
            if (FortranViewNumericContainerAction.isFortranIntrinsicTypeArray(
                    (children.getValue(0) as CidrPhysicalValue).type
            )) {
                is2D = true
                in2D = false
            } else {
                in2D = true
            }

            for (i in 0 until children.size()) {
                if (is2D){
                    if (in2D) {
                        val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                        val value = cidrValue.getVarData(context).value
                        if (rowN == 0) {
                            data.add(mutableListOf())
                            rowNames.add((children.getValue(i) as CidrPhysicalValue).name)
                        }
                        data[i].add(value)
                    } else {
                        children.getValue(i).computeChildren(this)
                        (children.getValue(i) as CidrPhysicalValue).process.postCommand {
                            colNames.add((children.getValue(i) as CidrPhysicalValue).name)
                            rowN++;
                        }
                    }
                } else {
                    val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                    val value = cidrValue.getVarData(context).value
                    data.add(mutableListOf(value))
                    rowNames.add(cidrValue.name)
                }
            }
            if (is2D && in2D) return
            (children.getValue(0) as CidrPhysicalValue).process.postCommand {
                val dataArrayList = mutableListOf<Array<String>>()
                for (i in data) {
                    dataArrayList.add(i.toTypedArray())
                }
                builder.setData(dataArrayList.toTypedArray())

                // min max
                var min: Double? = null
                var max: Double? = null
                for (i in data) {
                    for(j in i) {
                        val number = java.lang.Double.parseDouble(j)
                        min = if (min != null) minOf(min, number) else number
                        max = if (max != null) maxOf(max, number) else number
                    }
                }

                val colHeaders = mutableListOf<ArrayChunk.ColHeader>()
                if (is2D) {
                    builder.setRows(data.size)
                    builder.setColumns(data[0].size)
                    for (i in colNames) {
                        colHeaders.add(ArrayChunk.ColHeader(i, arrayType, "%.5f", max.toString(), min.toString()))
                    }
                } else {
                    builder.setRows(children.size())
                    builder.setColumns(1)
                    colHeaders.add(ArrayChunk.ColHeader("1", arrayType, "%.5f", max.toString(), min.toString()))
                }

                builder.setFormat("%.5f")
                builder.setMin(min.toString())
                builder.setMax(max.toString())
                builder.setRowLabels(rowNames)
                builder.setColHeaders(colHeaders)
            }
        }

        override fun tooManyChildren(remaining: Int) {

        }

        override fun setErrorMessage(errorMessage: String) {

        }

        override fun setErrorMessage(errorMessage: String, link: XDebuggerTreeNodeHyperlink?) {
            setErrorMessage(errorMessage)
        }

        override fun setMessage(message: String,
                                icon: Icon?,
                                attributes: SimpleTextAttributes,
                                link: XDebuggerTreeNodeHyperlink?) {

        }

        override fun isObsolete(): Boolean {
            return false
        }

        override fun setAlreadySorted(alreadySorted: Boolean) {}
    }
}
