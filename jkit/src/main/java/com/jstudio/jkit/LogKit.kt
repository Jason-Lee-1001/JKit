package com.jstudio.jkit

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ArrayBlockingQueue
import kotlin.concurrent.thread

/**
 * Created by Jason
 */
class LogKit private constructor() {

    companion object {
        private var enable = BuildConfig.DEBUG
        private val instance = LogKit()

        //Save to file
        private lateinit var appRootPath: String
        private val logFileBuffer: StringBuffer by lazy { StringBuffer() }
        private var saveToFile = false
        private var logFileHandler: LogFileHandler? = null

        @JvmStatic
        fun enableLogger(enable: Boolean) {
            this.enable = enable
        }

        @JvmStatic
        fun enableLogToFile(context: Context, enable: Boolean) {
            this.saveToFile = enable
            appRootPath = if (isExternalStorageAvailable()) {
                context.applicationContext.getExternalFilesDir(null)?.absolutePath ?: context.applicationContext.filesDir.absolutePath
            } else {
                context.applicationContext.filesDir.absolutePath
            }
            if (logFileHandler == null) logFileHandler = LogFileHandler(appRootPath)
        }

        @JvmStatic
        fun isEnable(): Boolean = enable

        @JvmStatic
        fun v(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) {
                Log.v(tag, "$detail -> $msg")
                if (saveToFile) saveToFile(tag, "$detail -> $msg")
            } else {
                Log.v(tag, msg)
                if (saveToFile) saveToFile(tag, msg)
            }
        }

        @JvmStatic
        fun i(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) {
                Log.i(tag, "$detail -> $msg")
                if (saveToFile) saveToFile(tag, "$detail -> $msg")
            } else {
                Log.i(tag, msg)
                if (saveToFile) saveToFile(tag, msg)
            }
        }

        @JvmStatic
        fun d(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) {
                Log.d(tag, "$detail -> $msg")
                if (saveToFile) saveToFile(tag, "$detail -> $msg")
            } else {
                Log.d(tag, msg)
                if (saveToFile) saveToFile(tag, msg)
            }
        }

        @JvmStatic
        fun w(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) {
                Log.w(tag, "$detail -> $msg")
                if (saveToFile) saveToFile(tag, "$detail -> $msg")
            } else {
                Log.w(tag, msg)
                if (saveToFile) saveToFile(tag, msg)
            }
        }

        @JvmStatic
        fun e(tag: String, msg: String) {
            if (!enable) return
            val detail = instance.detail()
            if (detail != null) {
                Log.e(tag, "$detail -> $msg")
                if (saveToFile) saveToFile(tag, "$detail -> $msg")
            } else {
                Log.e(tag, msg)
                if (saveToFile) saveToFile(tag, msg)
            }
        }

        @JvmStatic
        fun e(tag: String, throwable: Throwable?) {
            if (!enable) return
            Log.e(tag, null, throwable)
            if (saveToFile && throwable != null) saveToFile(tag, Log.getStackTraceString(throwable))
        }

        private fun saveToFile(tag: String, msg: String) {
            try {
                val content = StringBuilder()
                    .append(System.currentTimeMillis().toPatternString("yyyyMMdd-HH:mm:ss:SSS"))
                    .append("|")
                    .append(tag)
                    .append("|")
                    .append(msg)
                    .append("\n")
                logFileBuffer.append(content.toString())
                synchronized(LogKit::class.java) {
                    val writeContent = logFileBuffer.toString()
                    logFileBuffer.delete(0, content.length)
                    logFileHandler?.appendContent(writeContent)
                }
            } catch (oom: OutOfMemoryError) {
            }
        }

        private fun getLogFile(): File? = File("$appRootPath${File.separator}log.txt").takeIf { it.exists() }
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

private class LogFileHandler(private val filePath: String) {
    private val divider = "==================================================================================\n"
    private val maxFileLength = 6 * 1024 * 1024
    private val blockingQueue = ArrayBlockingQueue<String>(100)
    private var fos: FileOutputStream? = null

    init {
        blockingQueue.add(divider)
        thread(name = "log_file_thread") {
            while (true) {
                try {
                    takeAndSave(blockingQueue.take())
                } catch (e: Exception) {
                }
            }
        }
    }

    fun appendContent(content: String) = try {
        blockingQueue.add(content)
    } catch (e: Exception) {
    }

    private fun takeAndSave(content: String) {
        val logFile = getLogFile()
        if (fos == null) {
            try {
                if (logFile != null) fos = FileOutputStream(logFile, true)
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "take and save failed")
            }
        }

        if (fos != null && logFile != null) {
            changeOutputStream(logFile, fos!!)
            try {
                val byteArray = content.toByteArray(Charsets.UTF_8)
                fos?.write(byteArray)
                fos?.flush()
            } catch (e: Exception) {
            }
        }
    }

    private fun changeOutputStream(file: File, out: FileOutputStream) {
        if (file.exists() && file.length() > maxFileLength) {
            Log.i(javaClass.simpleName, "rebuild file")
            try {
                out.flush()
                out.close()
                fos = null
            } catch (e: IOException) {
            }
            file.delete()
            fos = FileOutputStream(file, true)
        }
    }

    private fun getLogFile(): File? = File("${filePath}${File.separator}log.txt").apply { createNewFile() }.takeIf { it.exists() }
}

fun whenDebugging(action: () -> Unit) {
    if (LogKit.isEnable()) action()
}

fun Any.log(msg: Any, tag: String = this.javaClass.name) = LogKit.v(tag, msg.toString())