package com.jstudio.jkit.common

/**
 * Created by Jason
 */
infix fun <T> Boolean.then(value: T) = if (this) value else null

infix fun <T> Any?.otherWise(value: T): T = value

infix fun <T> Any?.otherWiseNullable(value: T?): T? = value