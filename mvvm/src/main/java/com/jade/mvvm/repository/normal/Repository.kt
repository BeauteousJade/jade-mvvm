package com.jade.mvvm.repository.normal

import androidx.lifecycle.MutableLiveData
import com.jade.mvvm.fragment.list.source.LoadStatus
import com.jade.mvvm.network.Request
import com.jade.mvvm.network.Response

interface Repository<T> {
    fun load()
    fun getLoadStatusLiveData(): MutableLiveData<LoadStatus>
    fun getLoadDataLiveData(): MutableLiveData<Response<T>>
}