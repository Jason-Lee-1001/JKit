package com.jstudio.app

import android.app.Application
import com.jstudio.app.entity.User
import com.jstudio.jkit.AppKit
import com.jstudio.jkit.ToastKit

class App : Application() {

    companion object {
        var user: User? = null
    }

    override fun onCreate() {
        super.onCreate()
        ToastKit.init(this)
        AppKit.obtain().init(this)
    }
}