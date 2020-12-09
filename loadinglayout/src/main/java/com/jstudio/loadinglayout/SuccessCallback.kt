package com.jstudio.loadinglayout

import android.content.Context
import android.view.View

/**
 * Created by Jason
 */
class SuccessCallback(view: View, context: Context, onReloadListener: OnReloadListener?) : Callback(view, context, onReloadListener) {
    override fun onCreateView(): Int = 0

    fun hide() {
        obtainRootView().visibility = View.INVISIBLE
    }

    fun show() {
        obtainRootView().visibility = View.VISIBLE
    }

    fun showWithCallback(successVisible: Boolean) {
        obtainRootView().visibility = if (successVisible) View.VISIBLE else View.INVISIBLE
    }
}