package com.jstudio.app.http

object ResultState {
    const val SUCCESS = 1
}

class ResultBody<T> {
    val state: Int = ResultState.SUCCESS
    val msg: String = ""
    val `data`: T? = null
    val array: MutableList<T>? = null
}