package com.jade.mvvm.fragment.list.source

import androidx.lifecycle.MutableLiveData
import com.jade.mvvm.fragment.list.helper.ListOperation

interface DataSourceAdapter<MODEL> : ListOperation<MODEL> {
    fun getLoadStatusLiveData(): MutableLiveData<LoadStatus>
}