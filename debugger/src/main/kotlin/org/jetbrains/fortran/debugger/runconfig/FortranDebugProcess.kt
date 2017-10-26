package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.SmartList
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
import com.jetbrains.cidr.execution.debugger.evaluation.CidrMemberValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.cidr.execution.debugger.evaluation.EvaluationContext
import com.jetbrains.python.debugger.*
import com.jetbrains.python.debugger.dataview.DataViewFrameAccessor
import com.jetbrains.python.debugger.dataview.DataViewValueHolder
import org.jetbrains.fortran.debugger.FortranLineBreakpointType
import org.jetbrains.fortran.debugger.dataView.FortranViewNumericContainerAction
import java.util.concurrent.CompletableFuture
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
        val context = createEvaluationContext(driverInTests, null, session.currentStackFrame as CidrStackFrame)

        val future : CompletableFuture<ArrayChunkBuilder> = CompletableFuture()
        val node = DataViewNode(context, future)

        // (holder.value as CidrPhysicalValue).computeChildren(context, node) gives some problems with hashes
        // we don't use hashing. As a result, this part works
        val physicalValue = holder.value as CidrPhysicalValue
        val childrenList = driverInTests.getVariableChildren(physicalValue.`var`, -1, -1).list
        val values = SmartList<CidrValue>()
        childrenList.mapTo(values) { CidrMemberValue(it, physicalValue, false) }
        CidrValue.addAllTo(values, node)

        return future.get(3000, TimeUnit.MILLISECONDS).setHolder(holder)
                .setSlicePresentation(holder.getName()).createArrayChunk()
    }


    override fun addFrameListener(listener: PyFrameListener) {
        myFrameListeners.add(listener)
    }



    private class DataViewNode(val context : EvaluationContext,
                               val future : CompletableFuture<ArrayChunkBuilder>) : XCompositeNode {
        private var arrayIs2D: Boolean = false
        private val data = mutableListOf<MutableList<String>>()
        private val rowNames = mutableListOf<String>()
        private val colNames = mutableListOf<String>()
        private val builder = ArrayChunkBuilder()

        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            val firstChildren = children.getValue(0) as CidrPhysicalValue
            val process = firstChildren.process

            // type
            val arrayType = computeArrayChunkType(firstChildren.type)
            builder.setType(arrayType)

            val lastLevel : Boolean
            if (FortranViewNumericContainerAction.isFortranIntrinsicTypeArray(firstChildren.type)) {
                arrayIs2D = true
                lastLevel = false
            } else {
                lastLevel = true
            }

            for (i in 0 until children.size()) {
                if (arrayIs2D){
                    if (lastLevel) {
                        val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                        if (data.size <= i) {
                            data.add(mutableListOf())
                            rowNames.add(cidrValue.name)
                        }
                        addToRow(cidrValue.getVarData(context).value, arrayType, i)
                    } else {
                        children.getValue(i).computeChildren(this)
                        process.postCommand {
                            colNames.add((children.getValue(i) as CidrPhysicalValue).name)
                        }
                    }
                } else {
                    val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                    addTo1DArray(cidrValue.getVarData(context).value, arrayType)
                    rowNames.add(cidrValue.name)
                }
            }
            if (arrayIs2D && lastLevel) return

            // prepare final result
            process.postCommand {
                val dataArrayList = data.map { it.toTypedArray() }
                builder.setData(dataArrayList.toTypedArray())

                val (min, max) = computeMinMax(arrayType)

                val colHeaders = mutableListOf<ArrayChunk.ColHeader>()

                if (arrayIs2D) {
                    colNames.mapTo(colHeaders) { ArrayChunk.ColHeader(it, arrayType, "%.5f", max, min) }
                } else {
                    colHeaders.add(ArrayChunk.ColHeader("1", arrayType, "%.5f", max, min))
                }

                builder.setRows(data.size)
                builder.setColumns(data[0].size)
                builder.setFormat("%.5f")
                builder.setMin(min).setMax(max)
                builder.setRowLabels(rowNames)
                builder.setColHeaders(colHeaders)
                future.complete(builder)
            }

        }

        private fun computeArrayChunkType(type : String) = when {
            type.contains("integer") -> "i"
            type.contains("real") -> "f"
            type.contains("logical") -> "b"
            else -> "c"
        }

        private fun addTo1DArray(valData : String, arrayType : String) {
            when (arrayType) {
                "c" -> data.add(mutableListOf(ComplexNumber(valData).toString()))
                "b" -> data.add(mutableListOf(if (valData.equals(".true.", true)) "True" else "False"))
                else -> data.add(mutableListOf(valData))
            }
        }

        private fun addToRow(valData : String, arrayType : String, row : Int) {
            when (arrayType) {
                "c" -> data[row].add(ComplexNumber(valData).toString())
                "b" -> data[row].add(if (valData.equals(".true.", true)) "True" else "False")
                else -> data[row].add(valData)
            }
        }

        private fun computeMinMax(type: String) : Pair<String, String> {
            return when (type) {
                "c" -> computeComplexMinMax()
                "b" -> Pair("False", "True")
                else -> computeDoubleMinMax()
            }
        }

        private fun computeComplexMinMax() : Pair<String, String> {
            var min: ComplexNumber? = null
            var max: ComplexNumber? = null
            for (i in data) {
                for(j in i) {
                    val signIndex = j.indexOfLast { it == '+' || it == '-' }
                    val number = ComplexNumber(j.substring(0,signIndex), j.substring(signIndex,j.lastIndex))
                    min = if (min != null) minOf(min, number) else number
                    max = if (max != null) maxOf(max, number) else number
                }
            }
            return Pair(min.toString(), max.toString())
        }

        private fun computeDoubleMinMax() : Pair<String, String> {
            var min: Double? = null
            var max: Double? = null
            for (i in data) {
                for(j in i) {
                    val number = j.toDouble()
                    min = if (min != null) minOf(min, number) else number
                    max = if (max != null) maxOf(max, number) else number
                }
            }
            return Pair(min.toString(), max.toString())
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


    private class ComplexNumber : Comparable<ComplexNumber> {
        constructor(fortranComplex: String) {
            val comaIndex = fortranComplex.indexOf(',')
            this.realPart = fortranComplex.substring(1, comaIndex).toDouble()
            this.imPart = fortranComplex.substring(comaIndex+1, fortranComplex.lastIndex).toDouble()
        }

        constructor(real : String, im: String) {
            this.realPart = real.toDouble()
            this.imPart = im.toDouble()
        }

        val realPart: Double
        val imPart: Double


        override fun toString() : String {
            return if (imPart < 0) realPart.toString() + imPart.toString() + "j"
            else realPart.toString() + "+" + imPart.toString() + "j"
        }

        override fun compareTo(other: ComplexNumber): Int {
            return if (realPart == other.realPart && imPart == other.imPart) {
                0
            } else if ((realPart < other.realPart || ((realPart == other.realPart && imPart < other.imPart)))) {
                1
            } else {
                -1
            }
        }
    }
}
