package com.jstudio.loadinglayout

import android.content.Context
import android.view.View
import java.io.*

abstract class Callback constructor(view: View?, private var context: Context?, private var onReloadListener: OnReloadListener?) : Serializable {

    constructor() : this(null, null, null)

    private var rootView: View? = view
    var successVisible = false

    fun setCallback(context: Context?, onReloadListener: OnReloadListener?): Callback {
        this.context = context
        this.onReloadListener = onReloadListener
        return this
    }

    fun getRootView(): View? {
        val resId = onCreateView()
        if (resId == 0 && rootView != null) return rootView
        if (onBuildView(context) != null) rootView = onBuildView(context)
        if (rootView == null) rootView = View.inflate(context, onCreateView(), null)
        rootView?.setOnClickListener(View.OnClickListener { v ->
            if (onReloadEvent(context, rootView)) return@OnClickListener
            onReloadListener?.onReload(v)
        })
        onViewCreate(context, rootView!!)
        return rootView
    }

    protected fun onBuildView(context: Context?): View? {
        return null
    }

    protected fun onReloadEvent(context: Context?, view: View?): Boolean {
        return false
    }

    open fun copy(): Callback {
        val bao = ByteArrayOutputStream()
        val oos: ObjectOutputStream
        var obj: Any? = null
        try {
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback
    }

    fun obtainRootView(): View {
        if (rootView == null) rootView = View.inflate(context, onCreateView(), null)
        return rootView!!
    }

    interface OnReloadListener : Serializable {
        fun onReload(v: View)
    }

    protected abstract fun onCreateView(): Int

    protected fun onViewCreate(context: Context?, view: View) {}

    fun onAttach(context: Context, view: View) {}

    fun onDetach() {}
}