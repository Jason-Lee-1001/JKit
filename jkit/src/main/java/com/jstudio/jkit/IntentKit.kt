package com.jstudio.jkit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import java.io.File

const val APK_MINE_TYPE = "application/vnd.android.package-archive"

fun Activity.showLauncher() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.restartApp() {
    val restartIntent = this.packageManager.getLaunchIntentForPackage(this.packageName)
    restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(restartIntent)
}

fun Intent.isAccessible(context: Context): Boolean {
    return context.packageManager?.let { it.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY).size > 0 } ?: false
}

/**
 * 调用 apk 安装器安装程序
 */
fun Context.installApk(context: Context, apkFile: File, authority: String) {
    when {
        //作用域存储
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
        }
        //请求安装 apk 权限
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
        }
        //FileProvider
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
        }
        //运行时权限
        Build.VERSION.SDK_INT == Build.VERSION_CODES.M -> {
        }
        else -> {

        }
    }
}

fun takePhoto() {

}

fun handleTakePhotoResult() {

}