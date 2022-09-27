@file:JvmName("TimeKit")

package com.jstudio.jkit

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jason
 */
private val patternPool = hashMapOf<String, SimpleDateFormat>()
private val tempCalendar = Calendar.getInstance()

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

var Calendar.dayOfYear: Int
    get() = get(Calendar.DAY_OF_YEAR)
    set(value) = set(Calendar.DAY_OF_YEAR, value)

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

fun Calendar.setToMax(field: Int): Calendar {
    this.set(field, this.getMaximum(field))
    return this
}

fun Calendar.setToMin(field: Int): Calendar {
    this.set(field, this.getMinimum(field))
    return this
}

fun Calendar.toPatternString(pattern: String): String {
    return this.timeInMillis.toPatternString(pattern)
}

fun Calendar.isSameDay(other: Calendar) = year == other.year && get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

fun Calendar.isSameWeek(other: Calendar) = year == other.year && get(Calendar.WEEK_OF_YEAR) == other.get(Calendar.WEEK_OF_YEAR)

fun Calendar.isSameMonth(other: Calendar) = year == other.year && get(Calendar.MONTH) == other.get(Calendar.MONTH)

fun Calendar.setTime(hourOfDay: Int, minute: Int, second: Int = 0, milliSec: Int = 0): Calendar {
    this.set(Calendar.HOUR_OF_DAY, hourOfDay)
    this.set(Calendar.MINUTE, minute)
    this.set(Calendar.SECOND, second)
    this.set(Calendar.MILLISECOND, milliSec)
    return this
}

fun Calendar.endTimeInMillisOfToday(): Long {
    tempCalendar.timeInMillis = this.timeInMillis
    tempCalendar.setTime(0, 0, 0, 0)
    tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
    return tempCalendar.timeInMillis
}

fun Calendar.startTimeInMillisOfToday(): Long {
    tempCalendar.timeInMillis = this.timeInMillis
    tempCalendar.setTime(0, 0, 0, 0)
    return tempCalendar.timeInMillis
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

@Throws(Exception::class)
fun String.convertPattern(sourcePattern: String, destPattern: String): String {
    var sourceDateFormat = patternPool[sourcePattern]
    if (sourceDateFormat == null) {
        sourceDateFormat = SimpleDateFormat(sourcePattern, Locale.getDefault())
        patternPool[sourcePattern] = sourceDateFormat
    }
    val sourceDate = sourceDateFormat.parse(this) ?: throw NullPointerException()
    return sourceDate.toPatternString(destPattern)
}