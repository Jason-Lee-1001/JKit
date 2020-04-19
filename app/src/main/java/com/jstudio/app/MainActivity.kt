package com.jstudio.app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSharedPreferences("", Context.MODE_PRIVATE).edit {
            this.putBoolean("", true)
        }
    }
}
