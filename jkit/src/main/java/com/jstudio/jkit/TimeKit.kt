@file:JvmName("TimeKit")

package com.jstudio.jkit

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jason
 */
private val patternPool = hashMapOf<String, SimpleDateFormat>()

var Calendar.year: Int
    get() = get(Calendar.YEAR)
    set(value) = set(Calendar.YEAR, value)

var Calendar.month: Int
    get() = get(Calendar.MONTH)
    set(value) = set(Calendar.MONTH, value)

var Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)
    set(value) = set(Calendar.DAY_OF_MONTH, value)

var Calendar.dayOfWeek: Int
    get() = get(Calendar.DAY_OF_WEEK)
    set(value) = set(Calendar.DAY_OF_WEEK, value)

var Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) = set(Calendar.HOUR_OF_DAY, value)

var Calendar.minute: Int
    get() = get(Calendar.MINUTE)
    set(value) = set(Calendar.MINUTE, value)

var Calendar.second: Int
    get() = get(Calendar.SECOND)
    set(value) = set(Calendar.SECOND, value)

fun Calendar.setDate(year: Int, monthFromZero: Int, day: Int): Calendar {
    this.year = year
    this.month = monthFromZero
    this.dayOfMonth = day
    return this
}

fun Calendar.setTime(hourOfDay: Int, minute: Int, second: Int = 0, milliSec: Int = 0): Calendar {
    this.set(Calendar.HOUR_OF_DAY, hourOfDay)
    this.set(Calendar.MINUTE, minute)
    this.set(Calendar.SECOND, second)
    this.set(Calendar.MILLISECOND, milliSec)
    return this
}

/**
 * 格式化 Long 日期
 */
fun Long.toPatternString(formatPattern: String): String = Date(this).toPatternString(formatPattern)

/**
 * 格式化 Date 日期
 */
fun Date.toPatternString(formatPattern: String): String {
    var dateFormat = patternPool[formatPattern]
    if (dateFormat == null) {
        dateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        patternPool[formatPattern] = dateFormat
    }
    return dateFormat.format(this)
}