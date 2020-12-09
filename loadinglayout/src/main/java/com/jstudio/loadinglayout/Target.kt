package com.jstudio.loadinglayout

/**
 * Created by Jason
 */
interface Target {
    fun replaceView(target: Any, onReloadListener: Callback.OnReloadListener?): LoadLayout
    override fun equals(other: Any?): Boolean
}