package org.jetbrains.fortran.debugger.dataView

import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueChildrenList
import com.jetbrains.cidr.execution.debugger.evaluation.CidrEvaluatedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrPhysicalValue
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.containerview.DataViewStrategy
import com.jetbrains.python.debugger.containerview.DataViewStrategyProvider
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess

class FortranDataViewStrategyProvider(val process: FortranDebugProcess) : DataViewStrategyProvider(setOf<DataViewStrategy>(FortranArrayViewStrategy())) {
    private var evaluatedExpression: String? = null

    /**
     * @return null if no strategy for this type
     */
    override fun getStrategy(value: XNamedValue): DataViewStrategy? {
        return if (FortranViewNumericContainerAction.isFortranTypeArray((value as CidrPhysicalValue).type))
            myStrategies.first()
        else
            null
    }

    @Throws(Exception::class)
    override fun loadCompletionVariants(): XValueChildrenList? {
        return process.loadCompletionVariants()
    }

    @Throws(Exception::class)
    override fun evaluate(expression: String): XNamedValue {
        evaluatedExpression = expression
        return process.evaluate(expression)
    }

    override fun getEvaluationError(e: Exception): String = e.message ?: ""


    @Throws(Exception::class)
    override fun getArrayItems(value: XNamedValue, rowOffset: Int, colOffset: Int, rows: Int, cols: Int, format: String): ArrayChunk {
        return process.getArrayItems(value, format)
    }

    override fun getType(value: XNamedValue): String {
        val type = (value as CidrPhysicalValue).type
        return when {
            type.contains("integer") -> "i"
            type.contains("real") -> "f"
            type.contains("logical") -> "b"
            else -> "c"
        }
    }

    override fun getTitle(value: XNamedValue): String = value.name

    override fun getSliceText(value: XNamedValue, chunk: ArrayChunk): String =
            if (value is CidrEvaluatedValue) {
               evaluatedExpression ?: "result"
            } else {
                value.name
            }

    override fun isTemporary(value: XNamedValue): Boolean = false
}