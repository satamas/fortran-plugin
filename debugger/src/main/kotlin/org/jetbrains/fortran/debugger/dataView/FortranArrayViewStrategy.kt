package org.jetbrains.fortran.debugger.dataView

import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.array.AsyncArrayTableModel
import com.jetbrains.python.debugger.containerview.ColoredCellRenderer
import com.jetbrains.python.debugger.containerview.DataViewStrategy
import com.jetbrains.python.debugger.containerview.DataViewerPanel
import com.jetbrains.python.debugger.dataframe.DataFrameTableCellRenderer

open class FortranArrayViewStrategy : DataViewStrategy() {

    override fun createTableModel(rowCount: Int, columnCount: Int, dataProvider: DataViewerPanel, debugValue: XNamedValue): AsyncArrayTableModel {
        return FortranTableModel(rowCount, columnCount, dataProvider, debugValue, this)
    }

    override fun createCellRenderer(minValue: Double, maxValue: Double, arrayChunk: ArrayChunk): ColoredCellRenderer {
        return DataFrameTableCellRenderer()
    }

    override fun isNumeric(dtypeKind: String): Boolean {
        return true
    }

    override fun getTypeName(): String {
        return FORTRAN_ARRAY
    }

    companion object {
        private val FORTRAN_ARRAY = "FortranArray"
    }
}