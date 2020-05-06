package com.jstudio.app.list

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jstudio.app.entity.Record

class RecordFactory: DataSource.Factory<Int, Record>() {

    private val sourceLiveData = MutableLiveData<RecordDataSource>()

    override fun create(): DataSource<Int, Record> {
        val recordDataSource = RecordDataSource()
        sourceLiveData.postValue(recordDataSource)
        return recordDataSource
    }
}