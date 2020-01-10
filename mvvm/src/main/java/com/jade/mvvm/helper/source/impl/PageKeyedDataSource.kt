package com.jade.mvvm.helper.source.impl

import androidx.paging.PageKeyedDataSource

// Todo 未来实现
class BasePageKeyedDataSource<KEY, MODEL> : PageKeyedDataSource<KEY, MODEL>() {
    override fun loadInitial(params: LoadInitialParams<KEY>, callback: LoadInitialCallback<KEY, MODEL>) {

    }

    override fun loadAfter(params: LoadParams<KEY>, callback: LoadCallback<KEY, MODEL>) {
    }

    override fun loadBefore(params: LoadParams<KEY>, callback: LoadCallback<KEY, MODEL>) {
    }
}