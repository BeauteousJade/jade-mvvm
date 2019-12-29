package com.jade.mvvm.helper.source

import androidx.paging.DataSource
import com.jade.mvvm.helper.source.helper.DataSourceAdapter

abstract class BaseDataSourceFactory<KEY, MODEL> : DataSource.Factory<KEY, MODEL>() {

    private val mDataSource by lazy { createDataSource() }

    final override fun create(): DataSource<KEY, MODEL> = mDataSource

    abstract fun createDataSource(): DataSource<KEY, MODEL>

    fun asDataSourceAdapter() = mDataSource as? DataSourceAdapter<*>
}