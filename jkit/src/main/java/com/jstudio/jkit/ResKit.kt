@file:JvmName("ResKit")

package com.jstudio.jkit

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Created by Jason
 */
fun Context.strRes(@StringRes resId: Int): String = getString(resId)

fun Fragment.strRes(@StringRes resId: Int): String = activity?.strRes(resId) ?: ""

fun Context.colorRes(@ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) getColor(resId)
    else resources.getColor(resId)
}

fun Fragment.colorRes(@ColorRes resId: Int): Int = activity?.colorRes(resId) ?: Color.parseColor("#FFFFFFFF")