package com.jstudio.loadinglayout

import android.view.View
import android.view.ViewGroup

/**
 * Created by Jason
 */
class ViewTarget : Target {
    override fun replaceView(target: Any, onReloadListener: Callback.OnReloadListener?): LoadLayout {
        val oldContent = target as View
        val contentParent = oldContent.parent as ViewGroup
        var childIndex = 0
        val childCount = contentParent.childCount
        for (i in 0 until childCount) {
            if (contentParent.getChildAt(i) === oldContent) {
                childIndex = i
                break
            }
        }
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context, onReloadListener)
        loadLayout.setupSuccessLayout(SuccessCallback(oldContent, oldContent.context, onReloadListener))
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }

    override fun equals(other: Any?): Boolean = other is View
}