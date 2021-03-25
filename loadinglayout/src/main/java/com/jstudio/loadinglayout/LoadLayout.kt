package com.jstudio.loadinglayout

import android.content.Context
import android.os.Looper
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Created by Jason
 */
class LoadLayout(context: Context) : FrameLayout(context) {

    private val callbacks: MutableMap<Class<out Callback>, Callback> = hashMapOf()

    private var onReloadListener: Callback.OnReloadListener? = null
    private var preCallback: Class<out Callback>? = null
    private var curCallback: Class<out Callback>? = null

    constructor(context: Context, onReloadListener: Callback.OnReloadListener?) : this(context) {
        this.onReloadListener = onReloadListener
    }

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        successView!!.visibility = INVISIBLE
        addView(successView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        curCallback = SuccessCallback::class.java
    }

    fun setupCallback(callback: Callback) {
        val cloneCallback: Callback = callback.copy()
        cloneCallback.setCallback(context, onReloadListener)
        addCallback(cloneCallback)
    }

    fun addCallback(callback: Callback) {
        if (callbacks.containsKey(callback.javaClass).not()) callbacks[callback.javaClass] = callback
    }

    fun showCallback(callback: Class<out Callback>) {
        checkCallbackExist(callback)
        if (isMainThread()) showCallbackView(callback) else post { showCallbackView(callback) }
    }

    fun getCurrentCallback(): Class<out Callback>? = curCallback

    private fun showCallbackView(status: Class<out Callback>) {
        preCallback?.let {
            if (it == status) return
            callbacks[it]?.onDetach()
        }
        TransitionManager.beginDelayedTransition(this)
        if (childCount > 1) removeViewAt(CALLBACK_CUSTOM_INDEX)
        for (key in callbacks.keys) {
            if (key == status) {
                val successCallback = callbacks[SuccessCallback::class.java] as SuccessCallback
                if (key == SuccessCallback::class.java) {
                    successCallback.show()
                } else {
                    successCallback.showWithCallback(callbacks[key]?.successVisible == true)
                    val rootView = callbacks[key]!!.getRootView()
                    addView(rootView)
                    callbacks[key]!!.onAttach(context, rootView!!)
                }
                preCallback = status
            }
        }
        curCallback = status
    }

    fun findViewByCallback(callback: Class<out Callback>): View? = callbacks[callback]?.obtainRootView()

    private fun checkCallbackExist(callback: Class<out Callback>) {
        require(callbacks.containsKey(callback)) { "The Callback (${callback.simpleName}) is nonexistent." }
    }

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
        fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()
    }
}
