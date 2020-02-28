package com.jade.mvvm.repository.list

import com.jade.mvvm.network.Request
import com.jade.mvvm.network.RequestCallback

abstract class BaseListPositionRepository<T> :
    ListPositionRepository<T> {

    override fun loadInit(size: Int, requestCallback: RequestCallback<T>) =
        getLoadInitRequest(size).enqueue(requestCallback)

    override fun loadMore(startPosition: Int, loadSize: Int, requestCallback: RequestCallback<T>) =
        getLoadMoreRequest(startPosition, loadSize).enqueue(requestCallback)

    abstract fun getLoadInitRequest(size: Int): Request<T>

    abstract fun getLoadMoreRequest(startPosition: Int, loadSize: Int): Request<T>
}