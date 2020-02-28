package com.jade.mvvm.helper.source.helper

import androidx.lifecycle.MutableLiveData

interface DataSourceAdapter {
    fun getLoadStatusLiveData(): MutableLiveData<LoadStatus>
}