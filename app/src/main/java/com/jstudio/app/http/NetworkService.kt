package com.jstudio.app.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object NetworkService {

    private val retrofit by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .callTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
                //添加 header 拦截器
            .build()
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://v.eachdoctor.com:9004/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = retrofit.create<ApiService>()
}