package com.jade.mvvm.helper.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

abstract class BaseDataSourceFactory<KEY, MODEL> : DataSource.Factory<KEY, MODEL>() {

    val mDataSourceLiveData = MutableLiveData<DataSource<KEY, MODEL>>()

    final override fun create(): DataSource<KEY, MODEL> = createDataSource().apply {
        mDataSourceLiveData.postValue(this)
    }

    abstract fun createDataSource(): DataSource<KEY, MODEL>
}