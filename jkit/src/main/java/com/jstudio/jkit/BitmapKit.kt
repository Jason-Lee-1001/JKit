package com.jstudio.jkit

import android.graphics.Bitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Jason
 */
fun saveBitmap(bitmap: Bitmap, folder: String, name: String, quality: Int = 80, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG) {
    val path = when (format) {
        Bitmap.CompressFormat.JPEG -> "$folder${File.separator}$name.jpg"
        Bitmap.CompressFormat.PNG -> "$folder${File.separator}$name.png"
        Bitmap.CompressFormat.WEBP -> "$folder${File.separator}$name.webp"
    }
    try {
        val fos = FileOutputStream(path)
        val bos = BufferedOutputStream(fos)
        bitmap.compress(format, quality, bos)
        bos.flush()
        bos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}