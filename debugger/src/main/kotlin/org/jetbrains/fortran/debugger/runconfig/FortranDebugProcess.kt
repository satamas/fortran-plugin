package org.jetbrains.fortran.debugger.runconfig

import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.SmartList
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.frame.*
import com.intellij.xdebugger.impl.ui.tree.nodes.XEvaluationCallbackBase
import com.jetbrains.cidr.execution.RunParameters
import com.jetbrains.cidr.execution.debugger.CidrLocalDebugProcess
import com.jetbrains.cidr.execution.debugger.CidrStackFrame
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver
import com.jetbrains.cidr.execution.debugger.breakpoints.CidrBreakpointHandler
import com.jetbrains.cidr.execution.debugger.evaluation.CidrMemberValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.cidr.execution.debugger.evaluation.EvaluationContext
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.ArrayChunkBuilder
import org.jetbrains.fortran.debugger.FortranLineBreakpointType
import org.jetbrains.fortran.debugger.dataView.FortranViewNumericContainerAction
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.swing.Icon

class FortranDebugProcess(parameters: RunParameters, session: XDebugSession, consoleBuilder: TextConsoleBuilder)
    : CidrLocalDebugProcess(parameters, session, consoleBuilder) {

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
    @Throws(Exception::class)
    fun evaluate(expression: String): CidrValue {
        val future : CompletableFuture<CidrPhysicalValue> = CompletableFuture()
        val node = EvaluationNode(future)
        if (expression == "result") {
            throw Exception("Unable to evaluate evaluated value!")
        }
        evaluator?.evaluate(expression, object : XEvaluationCallbackBase() {
            override fun evaluated(result: XValue) {
                node.addChildren(XValueChildrenList.singleton("result", result), true)
            }

            override fun errorOccurred(errorMessage: String) {
                node.setErrorMessage(errorMessage)
            }
        }, null)
        try {
            return future.get(3000, TimeUnit.MILLISECONDS)
        } catch (e : TimeoutException) {
            throw Exception("Timeout exception")
        }
    }


    fun getArrayItems(value: XNamedValue, rowOffset: Int, colOffset: Int, rows: Int, cols: Int, format: String): ArrayChunk
    {
        val context = createEvaluationContext(driverInTests, null, session.currentStackFrame as CidrStackFrame)

        val future : CompletableFuture<ArrayChunkBuilder> = CompletableFuture()
        val node = DataViewNode(context, future)

        // (holder.value as CidrPhysicalValue).computeChildren(context, node) gives some problems with hashes
        // we don't use hashing. As a result, this part works
        val physicalValue = value as CidrPhysicalValue
        val childrenList = driverInTests.getVariableChildren(physicalValue.`var`, -1, -1).list
        val values = SmartList<CidrValue>()
        childrenList.mapTo(values) { CidrMemberValue(it, physicalValue, false) }
        CidrValue.addAllTo(values, node)

        return future.get(3000, TimeUnit.MILLISECONDS).setValue(value)
                .setSlicePresentation(value.name).createArrayChunk()
    }

    private abstract class DataViewNodeBase : XCompositeNode {
        protected fun computeArrayChunkType(type : String) = when {
            type.contains("integer") -> "i"
            type.contains("real") -> "f"
            type.contains("logical") -> "b"
            else -> "c"
        }

        override fun tooManyChildren(remaining: Int) {}

        override fun setErrorMessage(errorMessage: String) {}

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

    private class DataViewNode(val context : EvaluationContext,
                               val future : CompletableFuture<ArrayChunkBuilder>) : DataViewNodeBase() {

        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            val data = mutableListOf<MutableList<String>>()
            val rowNames = mutableListOf<String>()
            val colNames = mutableListOf<String>()
            val builder = ArrayChunkBuilder()

            val firstChildren = children.getValue(0) as CidrPhysicalValue
            val arrayType = computeArrayChunkType(firstChildren.type)
            builder.setType(arrayType)

            val arrayIs2D = FortranViewNumericContainerAction.isFortranTypeArray(firstChildren.type)

            for (i in 0 until children.size()) {
                if (arrayIs2D) {
                    val future : CompletableFuture<MutableList<String>> = CompletableFuture()
                    val futureNames : CompletableFuture<MutableList<String>> = CompletableFuture()
                    val node = DataView2DArrayLineNode(context, future, futureNames, i == 0)
                    children.getValue(i).computeChildren(node)
                    val newCol = future.get()
                    if (i == 0) {
                        rowNames.addAll(futureNames.get())
                        (0 until newCol.size).mapTo(data) { mutableListOf(newCol[it]) }
                    } else {
                        for (j in 0 until newCol.size) {
                            data[j].add(newCol[j])
                        }
                    }
                    colNames.add((children.getValue(i) as CidrPhysicalValue).name)
                } else {
                    val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                    addTo1DArray(data, cidrValue.getVarData(context).value, arrayType)
                    rowNames.add(cidrValue.name)
                }
            }

            // prepare final result
            val dataArrayList = data.map { it.toTypedArray() }
            builder.setData(dataArrayList.toTypedArray())

            val (min, max) = computeMinMax(data, arrayType)

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

        private fun addTo1DArray(data : MutableList<MutableList<String>>, valData : String, arrayType : String) {
            when (arrayType) {
                "c" -> data.add(mutableListOf(ComplexNumber(valData).toString()))
                "b" -> data.add(mutableListOf(if (valData.equals(".true.", true)) "True" else "False"))
                else -> data.add(mutableListOf(valData))
            }
        }

        private fun computeMinMax(data : MutableList<MutableList<String>>, type: String) : Pair<String, String> {
            return when (type) {
                "c" -> computeComplexMinMax(data)
                "b" -> Pair("False", "True")
                else -> computeDoubleMinMax(data)
            }
        }

        private fun computeComplexMinMax(data : MutableList<MutableList<String>>) : Pair<String, String> {
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

        private fun computeDoubleMinMax(data : MutableList<MutableList<String>>) : Pair<String, String> {
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
    }

    private class DataView2DArrayLineNode(val context : EvaluationContext,
                                          val future : CompletableFuture<MutableList<String>>,
                                          val futureNames : CompletableFuture<MutableList<String>>,
                                          val needToComputeNames: Boolean) : DataViewNodeBase() {

        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            val list = mutableListOf<String>()
            val rowNames = mutableListOf<String>()
            val arrayType = computeArrayChunkType((children.getValue(0) as CidrPhysicalValue).type)

            for (i in 0 until children.size()) {
                val cidrValue = (children.getValue(i) as CidrPhysicalValue)
                val cidrValueData = cidrValue.getVarData(context).value
                when (arrayType) {
                    "c" -> list.add(ComplexNumber(cidrValueData).toString())
                    "b" -> list.add(if (cidrValueData.equals(".true.", true)) "True" else "False")
                    else -> list.add(cidrValueData)
                }
                if (needToComputeNames) {
                    rowNames.add(cidrValue.name)
                }
            }
            future.complete(list)
            futureNames.complete(rowNames)
        }
    }

    private class EvaluationNode(val future : CompletableFuture<CidrPhysicalValue>) : DataViewNodeBase() {
        override fun addChildren(children: XValueChildrenList, last: Boolean) {
            val evaluatedValue = children.getValue(0) as CidrPhysicalValue
            future.complete(evaluatedValue)
        }
    }

    fun loadCompletionVariants(): XValueChildrenList? {
        val future : CompletableFuture<XValueChildrenList> = CompletableFuture()
        val node = CompletionNode(future)
        session.currentStackFrame?.computeChildren(node)
        return future.get(1000, TimeUnit.MILLISECONDS)
    }

    private class CompletionNode(val future : CompletableFuture<XValueChildrenList>) : DataViewNodeBase() {
          override fun addChildren(children: XValueChildrenList, last: Boolean) {
              future.complete(children)
          }
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
