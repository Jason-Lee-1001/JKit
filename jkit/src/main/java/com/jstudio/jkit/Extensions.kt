package com.jstudio.jkit

/**
 * Created by Jason
 */
/**
 * 判断条件后，true 则执行或返回 value 否则返回空
 */
infix fun <T> Boolean.then(value: T) = if (this) value else null

/**
 * 判断为空和不为空的逻辑
 */
inline fun <T : Any> T?.isNotNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) notNullAction(this) else nullAction.invoke()
}

/**
 * 判断为空和不为空的逻辑
 */
inline fun <T : Any> T?.isNull(nullAction: () -> Unit, notNullAction: (T) -> Unit = {}) {
    if (this == null) nullAction.invoke() else notNullAction(this)
}

/**
 * 数字类是否为空或者为0
 */
fun Number?.isNullOrZero(): Boolean = (this == null || this == 0)

/**
 * 数字类是否为空或者为某个传入的值
 */
fun Number?.isNullOr(num: Number): Boolean = (this == null || this.toDouble() == num.toDouble())

/**
 * 当满足条件时执行 action
 */
inline fun takeActionWhen(enable: Boolean, action: () -> Unit) {
    if (enable) action()
}
