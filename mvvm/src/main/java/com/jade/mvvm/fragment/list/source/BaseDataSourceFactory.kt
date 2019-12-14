package com.jade.mvvm.fragment.list.source

import androidx.paging.DataSource


abstract class BaseDataSourceFactory<KEY, MODEL> : DataSource.Factory<KEY, MODEL>() {

    private val mDataSource by lazy { createDataSource() }

    final override fun create(): DataSource<KEY, MODEL> = mDataSource

    abstract fun createDataSource(): DataSource<KEY, MODEL>

    fun asDataSourceAdapter() = mDataSource as? DataSourceAdapter<*>
}