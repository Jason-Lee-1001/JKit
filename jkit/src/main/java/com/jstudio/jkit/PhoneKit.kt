@file:JvmName("PhoneKit")

package com.jstudio.jkit

import android.content.Context
import android.provider.Settings

/**
 * Created by Jason
 */
fun Context.androidId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)