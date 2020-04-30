package com.jstudio.jkit

import android.content.Context
import android.view.View

/**
 * Created by Jason
 */
fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

fun getScreenHeight(context: Context): Int = context.resources.displayMetrics.heightPixels

inline fun View.onClick(crossinline function: () -> Unit) = setOnClickListener { function() }

inline fun View.singleTap(interval: Long = 500L, crossinline function: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - lastClick
        lastClick = System.currentTimeMillis()
        if (gap < interval) return@setOnClickListener
        function.invoke()
    }
}