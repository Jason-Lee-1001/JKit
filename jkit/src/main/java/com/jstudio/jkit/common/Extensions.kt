package com.jstudio.jkit.common

/**
 * Created by Jason
 */
infix fun <T> Boolean.then(value: T) = if (this) value else null

inline fun <T : Any> T?.isNotNull(notNullAction: (T) -> Unit, nullAction: () -> Unit) {
    if (this != null) {
        notNullAction(this)
    } else {
        nullAction.invoke()
    }
}