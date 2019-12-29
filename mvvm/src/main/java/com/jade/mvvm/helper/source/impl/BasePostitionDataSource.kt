package com.jade.mvvm.helper.source.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.jade.mvvm.helper.source.DataSourceSnapshot
import com.jade.mvvm.helper.source.helper.DataSourceAdapter
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ListPositionRepository

abstract class BasePostitionDataSource<MODEL>(private val listPositionRepository: ListPositionRepository<List<MODEL>>) :
    PositionalDataSource<MODEL>(),
    DataSourceAdapter<MODEL> {

    private val mDataSourceSnapshot = DataSourceSnapshot<MODEL>()
    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MODEL>) {
        val callBackInvoker: (list: List<MODEL>) -> Unit = {
            if (params.placeholdersEnabled) {
                callback.onResult(it, 0, getTotalCount())
            } else {
                callback.onResult(it, 0)
            }
        }
        if (mDataSourceSnapshot.isOperate()) {
            callBackInvoker.invoke(mDataSourceSnapshot.mModelList)
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING)
        listPositionRepository.loadInit(params.pageSize, object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                mDataSourceSnapshot.mModelList.clear()
                mDataSourceSnapshot.mModelList.addAll(t)
                callBackInvoker.invoke(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MODEL>) {
        if (mDataSourceSnapshot.isOperate()) {
            callback.onResult(mDataSourceSnapshot.mModelList)
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING)
        listPositionRepository.loadMore(
            params.startPosition,
            params.loadSize,
            object : RequestCallback<List<MODEL>> {
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
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.DEFAULT
        invalidate()
    }

    final override fun update(position: Int, model: MODEL) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.UPDATE
        mDataSourceSnapshot.mModelList[position] = model
        invalidate()
    }

    final override fun remove(list: List<MODEL>) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.REMOVE
        mDataSourceSnapshot.mModelList.removeAll(list)
        invalidate()
    }

    final override fun remove(position: Int) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.REMOVE
        mDataSourceSnapshot.mModelList.removeAt(position)
        invalidate()
    }

    final override fun add(list: List<MODEL>) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.ADD
        mDataSourceSnapshot.mModelList.addAll(list)
        invalidate()
    }

    final override fun add(position: Int, model: MODEL) {
        mDataSourceSnapshot.mOperateState =
            DataSourceSnapshot.ADD
        mDataSourceSnapshot.mModelList.add(position, model)
        invalidate()
    }

    final override fun getLoadStatusLiveData(): MutableLiveData<LoadStatus> = mLoadStatusLiveData

    open fun getTotalCount() = Int.MAX_VALUE
}