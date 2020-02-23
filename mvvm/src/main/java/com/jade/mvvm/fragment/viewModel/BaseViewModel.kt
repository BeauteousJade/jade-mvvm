package com.jade.mvvm.fragment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.jade.mvvm.fragment.list.helper.Operation
import com.jade.mvvm.repository.normal.Repository

abstract class BaseViewModel<MODEL>(val repository: Repository<MODEL>) : ViewModel(),
    Operation {

    val mLoadStatusLiveData = switchMap(repository.getLoadStatusLiveData()) {
        MutableLiveData(it)
    }
    val mLoadDataLiveData = switchMap(repository.getLoadDataLiveData()) {
        MutableLiveData(it)
    }

    fun trRefresh() {
        if (mLoadDataLiveData.value == null) {
            refresh()
        }
    }

    override fun refresh() = repository.load()
}