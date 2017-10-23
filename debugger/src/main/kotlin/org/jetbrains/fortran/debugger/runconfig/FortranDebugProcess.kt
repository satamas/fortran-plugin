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



    private class DataViewNode(val context : EvaluationContext, val builder: ArrayChunkBuilder, val varName : String) : XCompositeNode {

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

            val data = mutableListOf<Array<String>>()
            val rowNames = mutableListOf<String>()
            var min : Double? = null
            var max : Double? = null
            for (i in 0 until children.size()) {
                val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                val value = cidrValue.getVarData(context).value
                val number = java.lang.Double.parseDouble(value)
                min = if (min != null) minOf(min, number) else number
                max = if (max != null) maxOf(max, number) else number
                data.add(arrayOf(value))
                rowNames.add(cidrValue.name)
            }
            builder.setData(data.toTypedArray())
            builder.setRows(children.size())
            builder.setColumns(1)


            builder.setFormat("%.5f")
            builder.setMin(min.toString())
            builder.setMax(max.toString())
            builder.setRowLabels(rowNames)
            builder.setColHeaders(mutableListOf(ArrayChunk.ColHeader("1",arrayType,"%.5f",max.toString(), min.toString())))
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
