package org.jetbrains.fortran.debugger.dataView

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListenableFutureTask
import com.intellij.openapi.util.Pair
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.cidr.execution.debugger.evaluation.CidrValue
import com.jetbrains.python.debugger.ArrayChunk
import com.jetbrains.python.debugger.array.AsyncArrayTableModel
import com.jetbrains.python.debugger.containerview.DataViewerPanel
import com.jetbrains.python.debugger.dataframe.DataFrameTableModel
import org.jetbrains.fortran.debugger.runconfig.FortranDebugProcess


class FortranTableModel(rows: Int,
                        columns: Int,
                        provider: DataViewerPanel,
                        debugValue: XNamedValue,
                        strategy: FortranArrayViewStrategy)
    : DataFrameTableModel(rows, columns, provider, debugValue, strategy) {
    init {
        myChunkCache = CacheBuilder.newBuilder().build(
                object : CacheLoader<Pair<Int, Int>, ListenableFuture<ArrayChunk>>() {
                    @Throws(Exception::class)
                    override fun load(key: Pair<Int, Int>): ListenableFuture<ArrayChunk> {

                        return ListenableFutureTask.create {
                            val chunk = ((myDebugValue as CidrValue).process as FortranDebugProcess)
                                    .getArrayItems(myDebugValue, key.first, key.second, Math.min(AsyncArrayTableModel.CHUNK_ROW_SIZE, rowCount - key.first),
                                            Math.min(AsyncArrayTableModel.CHUNK_COL_SIZE, columnCount - key.second), myDataProvider.format)
                            handleChunkAdded(key.first, key.second, chunk)
                            chunk
                        }
                    }
                })

    }

    override fun isNumeric(): Boolean {
        return true
    }
}