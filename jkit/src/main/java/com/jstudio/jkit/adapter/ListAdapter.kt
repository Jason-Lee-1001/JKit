package com.jstudio.jkit.adapter

import android.content.Context

/**
 * Created by Jason
 */
abstract class ListAdapter<E>(protected val context: Context, collection: MutableList<E>?, animationType: Int = -1) :
    BaseRecyclerAdapter<MutableList<E>, E>(collection, animationType) {
    override fun getItemCount(): Int = collection?.size ?: 0
    override fun getItem(position: Int): E = collection!![position]
}