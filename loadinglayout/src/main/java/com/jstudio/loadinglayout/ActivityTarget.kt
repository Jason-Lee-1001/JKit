package com.jstudio.loadinglayout

import android.app.Activity
import android.view.ViewGroup

/**
 * Created by Jason
 */
class ActivityTarget : Target {

    override fun equals(other: Any?): Boolean = other is Activity

    override fun replaceView(target: Any, onReloadListener: Callback.OnReloadListener?): LoadLayout {
        val activity = target as Activity
        val contentParent = activity.findViewById<ViewGroup>(android.R.id.content)
        val childIndex = 0
        val originalContent = contentParent.getChildAt(childIndex)
        contentParent.removeView(originalContent)
        val oldLayoutParams = originalContent.layoutParams
        val loadLayout = LoadLayout(activity, onReloadListener)
        loadLayout.setupSuccessLayout(SuccessCallback(originalContent, activity, onReloadListener))
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}