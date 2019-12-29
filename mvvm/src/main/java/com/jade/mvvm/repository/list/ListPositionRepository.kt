package com.jade.mvvm.repository.list

import com.jade.mvvm.network.RequestCallback

interface ListPositionRepository<T> {
    fun loadInit(size: Int, requestCallback: RequestCallback<T>)
    fun loadMore(startPosition: Int, loadSize: Int, requestCallback: RequestCallback<T>)
}