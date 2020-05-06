package com.jstudio.jkit

import android.content.Context
import android.view.View

/**
 * Created by Jason
 */
fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

fun getScreenHeight(context: Context): Int = context.resources.displayMetrics.heightPixels

inline fun View.onClick(crossinline function: () -> Unit) = setOnClickListener { function() }

/**
 * dp值转换为px
 */
fun Context.dp2px(dp: Int): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

/**
 * px值转换成dp
 */
fun Context.px2dp(px: Int): Int = (px / resources.displayMetrics.density + 0.5f).toInt()

/**
 * dp值转换为px
 */
fun View.dp2px(dp: Int): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

/**
 * px值转换成dp
 */
fun View.px2dp(px: Int): Int = (px / resources.displayMetrics.density + 0.5f).toInt()

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

inline fun View.singleTap(interval: Long = 500L, crossinline function: () -> Unit) {
    var lastClick = 0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - lastClick
        lastClick = System.currentTimeMillis()
        if (gap < interval) return@setOnClickListener
        function.invoke()
    }
}