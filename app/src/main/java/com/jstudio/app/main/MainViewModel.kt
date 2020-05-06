package com.jstudio.app.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jstudio.app.entity.User
import com.jstudio.app.http.LoadState
import com.jstudio.app.http.NetworkService
import com.jstudio.app.tools.launch
import com.jstudio.jkit.desEncrypt
import com.jstudio.jkit.log
import com.jstudio.jkit.parseToJson
import kotlinx.coroutines.delay

class MainViewModel : ViewModel() {

    val userData = MutableLiveData<User>()
    val loadState = MutableLiveData<LoadState>()

    fun signIn(vararg pairs: Pair<String, String>) {
        launch({
            delay(2000)
            log("*******" + Thread.currentThread().name)
            val params = hashMapOf(*pairs)
            NetworkService.apiService.signIn(parseToJson(params).desEncrypt("edWY#)@F"))
        }, {
            loadState.value = LoadState.Start()
        }, {
            userData.value = this.data
            loadState.value = LoadState.Success()
        }, {
            loadState.value = LoadState.Fail()
            log(it.toString())
        }, {
            loadState.value = LoadState.End()
        })
    }
}