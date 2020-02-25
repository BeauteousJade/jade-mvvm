package com.jade.mvvm.repository.list

import com.jade.mvvm.network.Request
import com.jade.mvvm.network.RequestCallback

abstract class BaseItemKeyedRepository<KEY, MODEL> : ItemKeyedRepository<KEY, MODEL> {
    override fun loadInit(key: KEY?, size: Int, requestCallback: RequestCallback<MODEL>) {
        getRequest(key, size).enqueue(requestCallback)
    }

    abstract fun getRequest(key: KEY?, size: Int): Request<MODEL>
}