package com.jade.mvvm.repository.normal

import androidx.lifecycle.MutableLiveData
import com.jade.mvvm.helper.source.helper.LoadStatus

interface Repository<T> {
    fun load()
    fun getLoadStatusLiveData(): MutableLiveData<LoadStatus>
    fun getLoadDataLiveData(): MutableLiveData<T>
}