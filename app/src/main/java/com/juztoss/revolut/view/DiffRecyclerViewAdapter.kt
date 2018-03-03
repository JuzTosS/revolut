package com.juztoss.revolut.view

import android.os.Handler
import android.os.Looper
import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import java.util.*

/**
 * This adapter encapsulate DiffUtil processing,
 * allows us safely update data in RecyclerView using update(...) method.
 */
abstract class DiffRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    protected var data: List<T> = ArrayList()
    private val pendingUpdates = ArrayDeque<List<T>>()
    private val pendingUpdatesParameter = ArrayDeque<Boolean>()
    private val mHandler = Handler(Looper.getMainLooper())
    private var view: RecyclerView? = null

    @MainThread
    fun update(newData: List<T>?, scrollToTop: Boolean, view: RecyclerView) {
        this.view = view
        var dataToProcess: List<T>
        if (newData == null) {
            dataToProcess = ArrayList()
        } else {
            dataToProcess = newData
        }
        pendingUpdates.add(dataToProcess)
        pendingUpdatesParameter.add(scrollToTop)
        if (pendingUpdates.size == 1)
            internalUpdate(dataToProcess, scrollToTop, view)
    }

    private fun internalUpdate(newData: List<T>, scrollToTop: Boolean, view: RecyclerView?) {
        Thread {
            val result = DiffUtil.calculateDiff(Callback(data, newData), true)
            mHandler.post {
                this.data = newData
                result.dispatchUpdatesTo(this@DiffRecyclerViewAdapter)
                if (scrollToTop) {
                    view?.scrollToPosition(0)
                }
                processQueue()
            }
        }.start()
    }

    @MainThread
    private fun processQueue() {
        pendingUpdates.remove()
        pendingUpdatesParameter.remove()
        if (!pendingUpdates.isEmpty()) {
            if (pendingUpdates.size > 1) {
                val lastList = pendingUpdates.peekLast()
                val scrollToTop = pendingUpdatesParameter.peekLast()
                pendingUpdates.clear()
                pendingUpdates.add(lastList)
                pendingUpdatesParameter.clear()
                pendingUpdatesParameter.add(scrollToTop)
            }
            internalUpdate(pendingUpdates.peek(), pendingUpdatesParameter.peek(), view)
        } else {
            view = null
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private class Callback<T>(private val oldData: List<T>, private val newData: List<T>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldData.size
        }

        override fun getNewListSize(): Int {
            return newData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return if (oldData[oldItemPosition] == null || newData[newItemPosition] == null) {
                false
            } else {
                oldData[oldItemPosition] == newData[newItemPosition]
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame(oldItemPosition, newItemPosition)
        }
    }
}