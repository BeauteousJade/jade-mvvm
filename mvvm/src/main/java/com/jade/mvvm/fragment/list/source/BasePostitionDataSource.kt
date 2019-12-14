package com.jade.mvvm.fragment.list.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.jade.mvvm.fragment.list.helper.DataSourceSnapshot
import com.jade.mvvm.network.Request
import com.jade.mvvm.network.RequestCallback

abstract class BasePostitionDataSource<MODEL> : PositionalDataSource<MODEL>(), DataSourceAdapter<MODEL> {

    private val mDataSourceSnapshot = DataSourceSnapshot<MODEL>()
    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MODEL>) {
        if (mDataSourceSnapshot.isOperate()) {
            if (params.placeholdersEnabled) {
                callback.onResult(mDataSourceSnapshot.mModelList, 0, getTotalCount())
            } else {
                callback.onResult(mDataSourceSnapshot.mModelList, 0)
            }
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING)
        val requestCallback = object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                mDataSourceSnapshot.mModelList.clear()
                mDataSourceSnapshot.mModelList.addAll(t)
                if (params.placeholdersEnabled) {
                    callback.onResult(t, 0, getTotalCount())
                } else {
                    callback.onResult(t, 0)
                }
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        }
        onCreateInitialRequest(params.pageSize).enqueue(requestCallback)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MODEL>) {
        if (mDataSourceSnapshot.isOperate()) {
            callback.onResult(mDataSourceSnapshot.mModelList)
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING)
        onCreateRangeRequest(params.startPosition, params.loadSize).enqueue(object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                mDataSourceSnapshot.mModelList.addAll(t)
                callback.onResult(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    final override fun refresh() {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.DEFAULT
        invalidate()
    }

    final override fun update(position: Int, model: MODEL) {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.UPDATE
        mDataSourceSnapshot.mModelList[position] = model
        invalidate()
    }

    final override fun remove(list: List<MODEL>) {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.REMOVE
        mDataSourceSnapshot.mModelList.removeAll(list)
        invalidate()
    }

    final override fun remove(position: Int) {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.REMOVE
        mDataSourceSnapshot.mModelList.removeAt(position)
        invalidate()
    }

    final override fun add(list: List<MODEL>) {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.ADD
        mDataSourceSnapshot.mModelList.addAll(list)
        invalidate()
    }

    final override fun add(position: Int, model: MODEL) {
        mDataSourceSnapshot.mOperateState = DataSourceSnapshot.ADD
        mDataSourceSnapshot.mModelList.add(position, model)
        invalidate()
    }

    final override fun getLoadStatusLiveData(): MutableLiveData<LoadStatus> = mLoadStatusLiveData

    abstract fun onCreateInitialRequest(pageSize: Int): Request<List<MODEL>>

    abstract fun onCreateRangeRequest(startPosition: Int, pageSize: Int): Request<List<MODEL>>

    open fun getTotalCount() = Int.MAX_VALUE
}