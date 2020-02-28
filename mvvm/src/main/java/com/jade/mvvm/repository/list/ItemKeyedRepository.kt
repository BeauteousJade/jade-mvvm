package com.jade.mvvm.repository.list

import com.jade.mvvm.network.RequestCallback

interface ItemKeyedRepository<KEY, MODEL> {
    fun loadInit(key: KEY?, size: Int, requestCallback: RequestCallback<MODEL>)

    fun loadAfter(key: KEY?, size: Int, requestCallback: RequestCallback<MODEL>) {}

    fun loadBefore(key: KEY?, size: Int, requestCallback: RequestCallback<MODEL>) {}
}