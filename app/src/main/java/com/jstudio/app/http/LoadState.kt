package com.jstudio.app.http

sealed class LoadState(val msg: String) {
    class Start(msg: String = "") : LoadState(msg)
    class Loading(msg: String = "") : LoadState(msg)
    class Success(msg: String = "") : LoadState(msg)
    class Fail(msg: String = "") : LoadState(msg)
    class End(msg: String = "") : LoadState(msg)
}