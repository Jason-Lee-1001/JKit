package com.jstudio.app.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jstudio.app.entity.Record

class ListViewModel : ViewModel() {

    private val convertList: LiveData<PagedList<Record>>
    private val recordDataSource: DataSource<Int, Record>

    init {
        val recordFactory = RecordFactory()
        recordDataSource = recordFactory.create()
        convertList = LivePagedListBuilder(recordFactory, PagedList.Config.Builder().setPageSize(30).setInitialLoadSizeHint(30).build()).build()
    }

    fun invalidateDataSource() {
        recordDataSource.invalidate()
    }

    fun getConvertList(): LiveData<PagedList<Record>> {
        return convertList
    }
}