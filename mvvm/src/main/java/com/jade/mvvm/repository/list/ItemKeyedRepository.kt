package com.jade.mvvm.repository.list

import com.jade.mvvm.network.RequestCallback

interface ItemKeyedRepository<KEY, MODEL> {
    fun load(key: KEY?, size: Int, requestCallback: RequestCallback<MODEL>)
}