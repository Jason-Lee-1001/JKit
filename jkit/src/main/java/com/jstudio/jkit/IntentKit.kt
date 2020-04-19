package com.jstudio.jkit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.edit

fun Activity.forLauncher() {
    val intent = Intent()
    intent.action = Intent.CATEGORY_APP_BROWSER
    this.startActivity(intent)
    getSharedPreferences("", Context.MODE_PRIVATE).edit{

    }
}