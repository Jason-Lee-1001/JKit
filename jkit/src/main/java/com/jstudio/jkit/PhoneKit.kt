@file:JvmName("PhoneKit")

package com.jstudio.jkit

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

/**
 * Created by Jason
 */
fun Context.androidId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun supportX86(): Boolean = Build.SUPPORTED_ABIS?.contains("x86") ?: false

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun supportArmeabi(): Boolean = Build.SUPPORTED_ABIS?.contains("armeabi") ?: false

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun supportArmeabiV7a(): Boolean = Build.SUPPORTED_ABIS?.contains("armeabi-v7a") ?: false

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun supportArm64V8a(): Boolean = Build.SUPPORTED_ABIS?.contains("arm64-v8a") ?: false