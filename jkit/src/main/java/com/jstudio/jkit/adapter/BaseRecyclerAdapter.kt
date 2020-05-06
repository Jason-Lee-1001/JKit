package com.jstudio.jkit.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jstudio.jkit.ItemAnimation


abstract class BaseRecyclerAdapter<C, E>(protected var context: Context, var collection: C?, private var animationType: Int = -1) :
    RecyclerView.Adapter<BaseRecyclerAdapter.Holder>() {

    private var onItemClickBlock: ((v: View, p: Int, id: Long) -> Unit)? = null
    private var onItemLongClickBlock: ((v: View, p: Int, id: Long) -> Boolean)? = null
    private var onDataSetChangedBlock: ((c: Int) -> Unit)? = null

    private var onAttach = true
    private var lastAnimatePosition = -1
    private var observer: RecyclerView.AdapterDataObserver? = null

    abstract fun getItem(position: Int): E

    fun setOnItemClickListener(block: (view: View, position: Int, id: Long) -> Unit) {
        this.onItemClickBlock = block
    }

    fun setOnItemLongClickBlock(block: (view: View, position: Int, id: Long) -> Boolean) {
        this.onItemLongClickBlock = block
    }

    fun setDataSetChangedBlock(block: (count: Int) -> Unit) {
        this.onDataSetChangedBlock = block
    }

    fun setData(data: C?, diffCallback: DiffUtil.Callback? = null, detectMoves: Boolean = true) {
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
        val adapterPosition = holder.adapterPosition
        itemView.setOnClickListener {
            onItemClickBlock?.invoke(it, adapterPosition, getItemId(adapterPosition))
        }
        itemView.setOnLongClickListener {
            onItemLongClickBlock?.invoke(it, adapterPosition, getItemId(adapterPosition))
            false
        }
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        fillContent(holder, position, getItem(position))
        if (animationType > 0) setAnimation(holder.rootView, position)
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

    class Holder(val rootView: View, val viewType: Int) : RecyclerView.ViewHolder(rootView) {

        private val views: SparseArray<View> = SparseArray()

        fun <T : View> getView(@IdRes viewId: Int): T? {
            var view: View? = views.get(viewId)
            if (view == null) {
                view = rootView.findViewById(viewId)
                if (view != null) views.put(viewId, view)
            }
            return view as T?
        }

        fun setTextByString(viewId: Int, text: CharSequence): Holder {
            getView<TextView>(viewId)?.text = text
            return this
        }

        fun setTextByResource(viewId: Int, resId: Int): Holder {
            getView<TextView>(viewId)?.setText(resId)
            return this
        }
    }
}