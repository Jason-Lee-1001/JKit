package com.jstudio.app.list

import androidx.paging.PageKeyedDataSource
import com.jstudio.app.App
import com.jstudio.app.entity.Record
import com.jstudio.app.http.NetworkService
import com.jstudio.jkit.desEncrypt
import com.jstudio.jkit.parseToJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordDataSource : PageKeyedDataSource<Int, Record>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Record>) {
        val p = hashMapOf("doctorId" to App.user?.doctorId, "pageSize" to 30, "pageNum" to 1)
        GlobalScope.launch {
            val data = NetworkService.apiService.recordList(App.user!!.token, App.user!!.doctorId, parseToJson(p).desEncrypt("edWY#)@F")).array
            withContext(Dispatchers.Main) {
                data?.let { callback.onResult(data.toList(), null, 2) }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Record>) {
        val p = hashMapOf("doctorId" to App.user?.doctorId, "pageSize" to 30, "pageNum" to (params.key + 1))
        GlobalScope.launch {
            val data = NetworkService.apiService.recordList(App.user!!.token, App.user!!.doctorId, parseToJson(p).desEncrypt("edWY#)@F")).array
            withContext(Dispatchers.Main) {
                data?.let { callback.onResult(data.toList(), params.key + 1) }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Record>) {
        val p = hashMapOf("doctorId" to App.user?.doctorId, "pageSize" to 30, "pageNum" to (params.key - 1))
        GlobalScope.launch {
            val data = NetworkService.apiService.recordList(App.user!!.token, App.user!!.doctorId, parseToJson(p).desEncrypt("edWY#)@F")).array
            withContext(Dispatchers.Main) {
                data?.let { callback.onResult(data.toList(), (params.key - 1)) }
            }
        }
    }
}