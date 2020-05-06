package com.jstudio.app.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jstudio.app.http.ResultBody
import com.jstudio.jkit.ToastKit
import kotlinx.coroutines.*

inline fun <T> ViewModel.launch(
    crossinline block: suspend CoroutineScope.() -> ResultBody<T>,
    crossinline onStart: () -> Unit = {},
    crossinline onSuccess: ResultBody<T>.() -> Unit,
    crossinline onFail: (Throwable) -> Unit = { ToastKit.show(handleException(it)) },
    crossinline onEnd: () -> Unit = {}
) {
    viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
        onFail(throwable)
    }) {
        try {
            onStart()
            val resultBody = withContext(Dispatchers.IO) { block(this) }
            if (resultBody.state == 1) {
                onSuccess(resultBody)
            } else {
                throw Exception("request fail")
            }
        } finally {
            onEnd()
        }
    }
}

fun handleException(throwable: Throwable): String {
    return "${throwable.javaClass.simpleName} -> ${throwable.message ?: "Nah"}"
}