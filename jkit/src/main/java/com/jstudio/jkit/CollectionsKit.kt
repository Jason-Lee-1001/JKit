package com.jstudio.jkit

/**
 * Created by Jason
 */
/**
 * 可保留删除项目的 List
 */
class ListWithTrash<T>(private val innerList: MutableList<T> = arrayListOf()) : MutableList<T> by innerList {
    var deletedItem: T? = null

    override fun remove(element: T): Boolean {
        deletedItem = element
        return innerList.remove(element)
    }

    fun recover(): T? = deletedItem
}