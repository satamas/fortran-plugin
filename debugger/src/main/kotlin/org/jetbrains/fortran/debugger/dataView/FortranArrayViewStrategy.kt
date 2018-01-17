package org.jetbrains.fortran.debugger.dataView

import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.PyDebugValue
import com.jetbrains.python.debugger.array.AsyncArrayTableModel
import com.jetbrains.python.debugger.containerview.*
import com.jetbrains.python.debugger.dataframe.DataFrameTableCellRenderer
import com.jetbrains.python.debugger.dataframe.DataFrameTableModel
import com.jetbrains.python.debugger.dataframe.DataFrameViewStrategy

class FortranArrayViewStrategy : DataFrameViewStrategy() {
    override fun createTableModel(rowCount: Int, columnCount: Int, dataProvider: DataViewerPanel, debugValue: XNamedValue, provider: DataViewStrategyProvider): AsyncArrayTableModel {
        return DataFrameTableModel(rowCount, columnCount, dataProvider, debugValue as PyDebugValue, this, provider)
    }

    override fun createCellRenderer(minValue: Double, maxValue: Double, arrayChunk: ArrayChunk): ColoredCellRenderer {
        return DataFrameTableCellRenderer()
    }

    override fun getTypeName(): String {
        return FORTRAN_ARRAY
    }

    companion object {
        private val FORTRAN_ARRAY = "FortranArray"
    }
}