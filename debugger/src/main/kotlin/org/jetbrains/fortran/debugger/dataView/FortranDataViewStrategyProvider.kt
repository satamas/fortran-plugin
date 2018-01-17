package org.jetbrains.fortran.debugger.dataView

import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueChildrenList
import com.jetbrains.python.debugger.containerview.DataViewStrategy
import com.jetbrains.python.debugger.containerview.DataViewStrategyProvider
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess

class FortranDataViewStrategyProvider(val process: FortranDebugProcess) : DataViewStrategyProvider(setOf<DataViewStrategy>(FortranArrayViewStrategy())) {

    /**
     * @return null if no strategy for this type
     */
    override fun getStrategy(value: XNamedValue): DataViewStrategy? {
        val type = getType(value)
        for (strategy in myStrategies) {
            if (strategy.getTypeName().equals(type)) {
                return strategy
            }
        }
        return null
    }

    @Throws(Exception::class)
    override fun loadCompletionVariants(): XValueChildrenList {
        return frameAccessor.loadFrame()
    }

    @Throws(Exception::class)
    fun evaluate(expression: String): XNamedValue {
        val value = frameAccessor.evaluate(expression, false, true)
        if (value == null || value!!.isErrorOnEval()) {
            throw Exception(if (value != null) value!!.getValue() else "Failed to evaluate expression " + expression)
        }
        return value
    }

    fun getEvaluationError(e: Exception): String {
        return if (e is PyDebuggerException) {
            (e as PyDebuggerException).getTracebackError()
        } else {
            e.message
        }
    }

    @Throws(Exception::class)
    fun getArrayItems(`var`: XNamedValue, rowOffset: Int, colOffset: Int, rows: Int, cols: Int, format: String): ArrayChunk {
        return (`var` as PyDebugValue).getFrameAccessor().getArrayItems(`var` as PyDebugValue, rowOffset, colOffset, rows, cols, format)
    }

    fun getType(value: XNamedValue): String {
        return (value as PyDebugValue).getType()
    }

    fun getTitle(value: XNamedValue): String {
        return (value as PyDebugValue).getFullName()
    }

    fun getSliceText(value: XNamedValue, chunk: ArrayChunk): String {
        val pyValue = value as PyDebugValue
        return if (pyValue.getName().equals(pyValue.getTempName())) chunk.getSlicePresentation() else pyValue.getName()
    }

    fun isTemporary(value: XNamedValue): Boolean {
        return (value as PyDebugValue).isTemporary()
    }
}