package com.jstudio.jkit

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jason
 */
class TimeKit {
    companion object {
        private val patternPool = hashMapOf<String, SimpleDateFormat>()

        fun Long.formatDate(formatPattern: String): String {
            var dateFormat = patternPool[formatPattern]
            if (dateFormat == null) {
                dateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
                patternPool[formatPattern] = dateFormat
            }
            return dateFormat.format(Date(this))
        }
    }
}