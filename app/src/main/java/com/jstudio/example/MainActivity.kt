package com.jstudio.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jstudio.jkit.LogKit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogKit.enableLogger(true)
        LogKit.enableLogToFile(this, true)

        LogKit.v(javaClass.simpleName, "this is v msg")
        LogKit.i(javaClass.simpleName, "this is i msg")
        LogKit.d(javaClass.simpleName, "this is d msg")
        LogKit.w(javaClass.simpleName, "this is w msg")
        LogKit.e(javaClass.simpleName, "this is e msg")
    }
}
