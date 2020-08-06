package com.jstudio.jkit

import android.util.Log

/**
 * Created by Jason
 */
class LogKit private constructor() {

    companion object {
        private val instance = LogKit()

        private var enable: Boolean = BuildConfig.DEBUG


        @JvmStatic
        fun enableLogger(enable: Boolean) {
            this.enable = enable
        }

        @JvmStatic
        fun isEnable(): Boolean = enable

        @JvmStatic
        fun v(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) Log.v(tag, "$detail -> $msg") else Log.v(tag, msg)
        }

        @JvmStatic
        fun i(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) Log.i(tag, "$detail -> $msg") else Log.i(tag, msg)
        }

        @JvmStatic
        fun d(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) Log.d(tag, "$detail -> $msg") else Log.d(tag, msg)
        }

        @JvmStatic
        fun w(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) Log.w(tag, "$detail -> $msg") else Log.w(tag, msg)
        }

        @JvmStatic
        fun e(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) Log.e(tag, "$detail -> $msg") else Log.e(tag, msg)
        }
    }

    private fun detail(): String? {
        val stackTrace = Thread.currentThread().stackTrace
        stackTrace.forEach {
            if (it.isNativeMethod) return@forEach
            if (it.className == Thread::class.java.name) return@forEach
            if (it.className == LogKit::class.java.name) return@forEach
            if (it.className == Companion::class.java.name) return@forEach
            return "[(${it.fileName}:${it.lineNumber}) ${it.methodName}]"
        }
        return null
    }
}

fun whenDebugging(action: () -> Unit) {
    if (LogKit.isEnable()) action()
}

fun Any.log(msg: Any, tag: String = this.javaClass.name) = LogKit.v(tag, msg.toString())