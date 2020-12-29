package com.jstudio.jkit.adapter

import android.content.Context

/**
 * Created by Jason
 */
abstract class ListAdapter<E>(protected val context: Context, collection: MutableList<E>?, animationType: Int = -1) :
    BaseRecyclerAdapter<MutableList<E>, E>(collection, animationType) {
    override fun getItemCount(): Int = collection?.size ?: 0
    override fun getItem(position: Int): E = collection!![position]
    override fun addData(insertIndex: Int, newList: List<E>) {
        if (collection == null) collection = arrayListOf()
        if (insertIndex in 0 until itemCount) {
            collection?.addAll(insertIndex, newList)
            notifyItemRangeInserted(insertIndex, newList.size)
        } else {
            val startPosition = collection?.size ?: 0
            collection?.addAll(newList)
            notifyItemRangeInserted(startPosition, newList.size)
        }
    }

    open fun addData(newList: List<E>) {
        if (collection == null) collection = arrayListOf()
        val startPosition = collection?.size ?: 0
        addData(startPosition, newList)
    }

    override fun addData(newData: E) {
        if (collection == null) collection = arrayListOf()
        val startPosition = collection?.size ?: 0
        collection?.add(newData)
        notifyItemRangeInserted(startPosition, 1)
    }

    override fun removeData(index: Int) {
        if (index > itemCount || index < 0 || collection.isNullOrEmpty()) return
        collection?.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun removeData(data: E) {
        val index = collection?.indexOf(data) ?: return
        if (index >= 0) {
            collection?.remove(data)
            notifyItemRemoved(index)
        }
    }

    override fun setData(index: Int, data: E) {
        if (index > itemCount || collection.isNullOrEmpty()) return
        collection?.set(index, data)
        notifyItemChanged(index)
    }

    override fun clearData() {
        if (collection.isNullOrEmpty()) return
        val size = collection?.size ?: 0
        collection?.clear()
        notifyItemRangeRemoved(0, size)
    }
}