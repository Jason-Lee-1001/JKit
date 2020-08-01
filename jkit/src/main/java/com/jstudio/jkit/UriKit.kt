@file:JvmName("UriKit")

package com.jstudio.jkit

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

/**
 * Created by Jason
 */
/**
 * 获取 Uri 中的文件名
 */
fun getUriFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if ("content" == uri.scheme) {
        val cursor = context.contentResolver.query(uri, null, null, null, null) ?: return null
        cursor.use {
            if (it.moveToFirst()) result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) result = result!!.substring(cut + 1)
    }
    return result
}