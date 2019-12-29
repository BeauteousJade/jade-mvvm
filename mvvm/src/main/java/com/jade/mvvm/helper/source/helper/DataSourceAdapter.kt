package com.jade.mvvm.helper.source.helper

import androidx.lifecycle.MutableLiveData
import com.jade.mvvm.fragment.list.helper.ListOperation

interface DataSourceAdapter<MODEL> : ListOperation<MODEL> {
    fun getLoadStatusLiveData(): MutableLiveData<LoadStatus>
}