package com.jstudio.jkit

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.getSystemService

/**
 * Created by Jason
 */
fun Context.currentProcessName(): String {
    val systemService = getSystemService<ActivityManager>()
    systemService?.runningAppProcesses?.forEach { info ->
        if (info.uid == android.os.Process.myPid()) return info.processName
    }
    return ""
}

fun Context.versionCode(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    packageManager?.getPackageInfo(packageName, 0)?.longVersionCode ?: -1L
} else {
    packageManager?.getPackageInfo(packageName, 0)?.versionCode?.toLong() ?: -1L
}

fun Context.versionName(): String? = packageManager?.getPackageInfo(packageName, 0)?.versionName

fun Context.getAppInfo(context: Context, packageName: String): PackageInfo? {
    return try {
        packageManager?.getPackageInfo(packageName, PackageManager.GET_META_DATA)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}