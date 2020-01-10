package com.jade.mvvm.repository.normal

import androidx.lifecycle.MutableLiveData
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.network.Request
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.network.Response

abstract class BaseRepository<MODEL> : Repository<MODEL> {

    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()
    private val mLoadDataLiveData = MutableLiveData<Response<MODEL>>()

    final override fun load() {
        mLoadStatusLiveData.postValue(LoadStatus.LOADING_REFRESH)
        getRequest().enqueue(object : RequestCallback<MODEL> {
            override fun onResult(t: MODEL) {
                mLoadDataLiveData.postValue(Response(t, null))
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
            }

            override fun onError(throwable: Throwable) {
                mLoadDataLiveData.postValue(Response(null, throwable))
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    final override fun getLoadDataLiveData() = mLoadDataLiveData

    final override fun getLoadStatusLiveData() = mLoadStatusLiveData

    abstract fun getRequest(): Request<MODEL>
}