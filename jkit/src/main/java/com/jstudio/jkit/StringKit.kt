@file:JvmName("StringKit")

package com.jstudio.jkit

/**
 * Created by Jason
 */

/**
 * Combine some string with divider, not recommend for long string
 */
fun combineString(divider: String, vararg fragments: String?): String {
    return if (fragments.isNullOrEmpty()) ""
    else fragments.reduce { acc, s -> if (s.isNullOrEmpty()) acc else "$acc$divider$s" } ?: ""
}