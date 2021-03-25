package com.jstudio.loadinglayout

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View

/**
 * Created by Jason
 */
class LoadService<T> internal constructor(
    builder: LoadSir.Builder,
    private val loadLayout: LoadLayout,
    private var mapper: ((T) -> Class<out Callback>)? = null
) {

    init {
        val callbacks: List<Callback> = builder.callbacks
        val defaultCallback: Class<out Callback>? = builder.defaultCallback
        if (callbacks.isNotEmpty()) {
            for (callback in callbacks) loadLayout.setupCallback(callback)
        }
        Handler(Looper.getMainLooper()).post {
            defaultCallback?.let { loadLayout.showCallback(it) }
        }
    }

    fun showSuccess() = loadLayout.showCallback(SuccessCallback::class.java)

    fun showCallback(callback: Class<out Callback>) = loadLayout.showCallback(callback)

    fun showConvertedCallback(t: T) {
        requireNotNull(mapper) { "You haven't set the mapper." }
        loadLayout.showCallback(mapper!!.invoke(t))
    }

    fun getLoadLayout(): LoadLayout = loadLayout

    fun getCurrentCallback(): Class<out Callback>? = loadLayout.getCurrentCallback()

    fun setCallBack(callback: Class<out Callback>, transport: ((Context, View) -> Unit)): LoadService<T> {
        loadLayout.findViewByCallback(callback)?.let { view -> transport.invoke(loadLayout.context, view) }
        return this
    }
}