package com.jstudio.jkit.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<C, E>(protected var context: Context, var collection: C?) :
    RecyclerView.Adapter<BaseRecyclerAdapter.Holder>() {

    private var onItemClickBlock: ((v: View, p: Int, id: Long) -> Unit)? = null
    private var onItemLongClickBlock: ((v: View, p: Int, id: Long) -> Boolean)? = null
    private var onDataSetChangedBlock: ((c: Int) -> Unit)? = null

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
        if (observer == null) {
            observer = object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    onDataSetChangedBlock?.invoke(itemCount)
                }
            }
        }
        registerAdapterDataObserver(observer!!)
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

    override fun onBindViewHolder(holder: Holder, position: Int) = fillContent(holder, position, getItem(position))

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