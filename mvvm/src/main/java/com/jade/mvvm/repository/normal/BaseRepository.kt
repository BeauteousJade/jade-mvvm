package com.jade.mvvm.repository.normal

import androidx.lifecycle.MutableLiveData
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.network.Request
import com.jade.mvvm.network.RequestCallback

abstract class BaseRepository<MODEL> : Repository<MODEL> {

    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()
    private val mLoadDataLiveData = MutableLiveData<MODEL>()

    final override fun load() {
        mLoadStatusLiveData.postValue(LoadStatus.LOADING_REFRESH)
        getRequest().enqueue(object : RequestCallback<MODEL> {
            override fun onResult(t: MODEL) {
                mLoadDataLiveData.postValue(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    final override fun getLoadDataLiveData() = mLoadDataLiveData

    final override fun getLoadStatusLiveData() = mLoadStatusLiveData

    abstract fun getRequest(): Request<MODEL>
}