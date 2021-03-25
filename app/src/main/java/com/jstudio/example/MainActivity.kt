package com.jstudio.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jstudio.loadinglayout.Callback
import com.jstudio.loadinglayout.LoadService
import com.jstudio.loadinglayout.LoadSir
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var loadService: LoadService<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LoadSir.create().addCallback(LoadingCallback()).buildDefault()
        setContentView(R.layout.activity_main)
        loadService = LoadSir.default.register(recyclerView)
    }

    companion object {
        class LoadingCallback : Callback() {
            override fun onCreateView(): Int = R.layout.loading
        }
    }
}
