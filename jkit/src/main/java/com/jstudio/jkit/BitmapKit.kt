@file:JvmName("BitmapKit")

package com.jstudio.jkit

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import androidx.annotation.RequiresApi
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Jason
 */
/**
 * 保存 Bitmap 到指定路径
 */
fun saveBitmap(bitmap: Bitmap, folder: String, name: String, quality: Int = 80, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): String? {
    val path = when (format) {
        Bitmap.CompressFormat.JPEG -> "$folder${File.separator}$name"
        Bitmap.CompressFormat.PNG -> "$folder${File.separator}$name"
        Bitmap.CompressFormat.WEBP -> "$folder${File.separator}$name"
        else -> "$folder${File.separator}$name"
    }
    return try {
        val fos = FileOutputStream(path)
        val bos = BufferedOutputStream(fos)
        bitmap.compress(format, quality, bos)
        bos.flush()
        bos.close()
        path
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

/**
 * 生成 View 的截图
 */
fun getBitmapFromView(view: View, defaultColor: Int = -1): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    if (defaultColor >= 0) canvas.drawColor(defaultColor)
    view.draw(canvas)
    return bitmap
}

/**
 * 根据传入的长宽，得到最佳inSimpleSize
 *
 * @param options       在调用此方法前通过BitmapFactory decode中的参数BitmapFactory.Options
 * @param requestWidth  输出宽度
 * @param requestHeight 输出高度
 * @return BitmapFactory.Options 中 inSimpleSize的值
 */
fun getBitmapSampleSize(options: BitmapFactory.Options, requestWidth: Int, requestHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > requestHeight || width > requestWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while (halfHeight / inSampleSize > requestHeight && halfWidth / inSampleSize > requestWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

/**
 * 使用像素复制生成 View 的 Bitmap
 */
@RequiresApi(Build.VERSION_CODES.O)
fun getBitmapFromView(view: View, activity: Activity, callback: (Bitmap) -> Unit) {
    activity.window?.let { window ->
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val viewPosInWindow = IntArray(2)
        view.getLocationInWindow(viewPosInWindow)
        try {
            PixelCopy.request(
                window, Rect(viewPosInWindow[0], viewPosInWindow[1], viewPosInWindow[0] + view.width, viewPosInWindow[1] + view.height),
                bitmap, { copyResult -> if (copyResult == PixelCopy.SUCCESS) callback(bitmap) }, Handler()
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}