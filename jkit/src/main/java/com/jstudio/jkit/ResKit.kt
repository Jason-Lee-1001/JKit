@file:JvmName("ResKit")

package com.jstudio.jkit

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created by Jason
 */
fun Context.strRes(@StringRes resId: Int): String = getString(resId)

fun Fragment.strRes(@StringRes resId: Int): String = activity?.strRes(resId) ?: ""

fun Context.colorRes(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

fun Fragment.colorRes(@ColorRes resId: Int): Int = activity?.colorRes(resId) ?: Color.parseColor("#FFFFFFFF")