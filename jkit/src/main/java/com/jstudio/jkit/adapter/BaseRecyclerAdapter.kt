package com.jstudio.jkit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jstudio.jkit.ItemAnimation
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer


abstract class BaseRecyclerAdapter<C, E>(var collection: C?, private var animationType: Int = -1) :
    RecyclerView.Adapter<BaseRecyclerAdapter.Holder>() {

    var onItemClickBlock: ((v: View, p: Int, id: Long) -> Unit)? = null
    var onItemLongClickBlock: ((v: View, p: Int, id: Long) -> Boolean)? = null
    var onDataSetChangedBlock: ((c: Int) -> Unit)? = null

    private var onAttach = true
    private var lastAnimatePosition = -1
    private var observer: RecyclerView.AdapterDataObserver? = null

    abstract fun getItem(position: Int): E

    open fun setData(data: C?, diffCallback: DiffUtil.Callback? = null, detectMoves: Boolean = true) {
        collection = data
        if (diffCallback == null) notifyDataSetChanged()
        else DiffUtil.calculateDiff(diffCallback, detectMoves).dispatchUpdatesTo(this)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (observer == null) {
            observer = object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    onDataSetChangedBlock?.invoke(itemCount)
                }
            }
        }
        registerAdapterDataObserver(observer!!)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        observer?.let {
            try {
                unregisterAdapterDataObserver(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(setViewLayout(viewType), parent, false)
        val holder = Holder(itemView, viewType)
        itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            onItemClickBlock?.invoke(it, adapterPosition, getItemId(adapterPosition))
        }
        itemView.setOnLongClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnLongClickListener false
            return@setOnLongClickListener onItemLongClickBlock?.invoke(it, adapterPosition, getItemId(adapterPosition)) ?: false
        }
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        fillContent(holder, position, getItem(position))
        if (animationType > 0) setAnimation(holder.containerView, position)
    }

    private fun setAnimation(rootView: View, position: Int) {
        if (position > lastAnimatePosition) {
            when (animationType) {
                1 -> ItemAnimation.fadeIn(rootView, if (onAttach) position else -1)
                2 -> ItemAnimation.rightToLeftFadeIn(rootView, if (onAttach) position else -1)
            }
            this.lastAnimatePosition = position
        }
    }

    abstract fun setViewLayout(type: Int): Int

    abstract fun fillContent(holder: Holder, position: Int, entity: E)

    @ContainerOptions(CacheImplementation.SPARSE_ARRAY)
    class Holder(override val containerView: View, val viewType: Int) : RecyclerView.ViewHolder(containerView), LayoutContainer
}