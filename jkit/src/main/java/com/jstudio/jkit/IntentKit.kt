@file:JvmName("IntentKit")

package com.jstudio.jkit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File

const val APK_MINE_TYPE = "application/vnd.android.package-archive"

/**
 * 回到桌面
 */
fun Activity.showLauncher() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

/**
 * 重启应用
 */
fun Context.restartApp() {
    val restartIntent = this.packageManager.getLaunchIntentForPackage(this.packageName)
    restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    restartIntent?.let { startActivity(it) }
}

/**
 * 是否有权限安装应用
 */
fun Context.canInstall(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) packageManager.canRequestPackageInstalls() else true

/**
 * intent 是否能被接收
 */
fun Intent.isAccessible(context: Context): Boolean = context.packageManager?.let {
    it.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY).size > 0
} ?: false

/**
 * 调用 apk 安装器安装程序
 */
fun Context.installApk(context: Context, apkFile: File, authority: String): Boolean {
    val install = Intent(Intent.ACTION_VIEW)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val apkUri = FileProvider.getUriForFile(this, authority, apkFile)
        install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        install.setDataAndType(apkUri, "application/vnd.android.package-archive")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return if (context.canInstall() && install.isAccessible(this)) {
                startActivity(install)
                true
            } else {
                val packageUri = Uri.parse("package:$packageName")
                val requestIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
                requestIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (requestIntent.isAccessible(this)) startActivity(requestIntent)
                false
            }
        } else {
            return if (install.isAccessible(this)) {
                startActivity(install)
                true
            } else {
                false
            }
        }
    } else {
        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return if (install.isAccessible(this)) {
            startActivity(install)
            true
        } else {
            false
        }
    }
}

/**
 * 使用系统自带相机拍照
 */
fun Activity.takePhoto(authority: String, destFile: File, requestCode: Int): Boolean {
    val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    return if (takePhoto.isAccessible(this)) {
        val fileUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Uri.fromFile(destFile)
        else FileProvider.getUriForFile(this, authority, destFile)
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(takePhoto, requestCode)
        true
    } else {
        false
    }
}

/**
 * 使用系统自带相机录制视频
 */
fun Activity.recordVideo(
    authority: String,
    destFile: File,
    requestCode: Int,
    quality: Int = 1,
    sizeLimit: Long = 1048576,
    durationLimit: Int = 10
): Boolean {
    val recordVideo = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    return if (recordVideo.isAccessible(this)) {
        val fileUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) Uri.fromFile(destFile)
        else FileProvider.getUriForFile(this, authority, destFile)
        recordVideo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        recordVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality)
        recordVideo.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit)
        recordVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit)
        startActivityForResult(recordVideo, requestCode)
        true
    } else {
        false
    }
}