package com.jstudio.jkit.adapter

import android.content.Context

/**
 * Created by Jason
 */
abstract class ListAdapter<E>(context: Context, collection: MutableList<E>, showItemAnimation: Boolean = false) :
    BaseRecyclerAdapter<MutableList<E>, E>(context, collection, showItemAnimation) {
    override fun getItemCount(): Int = collection?.size ?: 0
    override fun getItem(position: Int): E = collection!![position]
}