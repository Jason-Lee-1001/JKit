@file:JvmName("StringKit")

package com.jstudio.jkit

/**
 * Created by Jason
 */

/**
 * 组合字符串，并添加分隔符内容
 */
fun combineString(divider: String, vararg fragments: String?): String {
    return if (fragments.isNullOrEmpty()) ""
    else fragments.reduce { acc, s -> if (s.isNullOrEmpty()) acc else "$acc$divider$s" } ?: ""
}

/**
 * 是否纯数字
 */
fun String?.isDigital(): Boolean = this != null && this.matches(Regex("[0-9]+"))

/**
 * 是否为数值
 */
fun String?.isNumber(): Boolean = this != null && this.matches(Regex("\\d+||\\d*\\.\\d+||\\d*\\.?\\d+?e[+-]\\d*\\.?\\d+?||e[+-]\\d*\\.?\\d+?"))