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
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrBreakpointHandler
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.cidr.execution.debugger.evaluation.EvaluationContext
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.ArrayChunkBuilder
import com.jetbrains.python.debugger.PyDebuggerException
import com.jetbrains.python.debugger.PyFrameListener
import com.jetbrains.python.debugger.dataview.DataViewFrameAccessor
import com.jetbrains.python.debugger.dataview.DataViewValueHolder
import org.jetbrains.fortran.debugger.FortranLineBreakpointType
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
        val result = ArrayChunkBuilder().setHolder(holder)
        doGetArrayItems(holder, result)

        return result.createArrayChunk()
    }

    private fun doGetArrayItems(holder: DataViewValueHolder, result: ArrayChunkBuilder) {
        postCommand { driver ->
            val context = (holder.value as CidrPhysicalValue).createEvaluationContext(driver, null)
            val node = DataViewNode(result, context)
            (holder.value as CidrPhysicalValue).computeChildren(node)
        }
    }

    override fun addFrameListener(listener: PyFrameListener) {
        myFrameListeners.add(listener)
    }

    private class DataViewNode(val builder: ArrayChunkBuilder, private val myContext: EvaluationContext) : XCompositeNode {

        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            for (i in 0 until children.size()) {
                builder.setData(arrayOf(arrayOf(children)))
                builder.setRows(1)
                builder.setColumns(2)
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
