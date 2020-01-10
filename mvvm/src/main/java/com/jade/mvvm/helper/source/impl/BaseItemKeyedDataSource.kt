package com.jade.mvvm.helper.source.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.jade.mvvm.helper.source.PageModel
import com.jade.mvvm.helper.source.helper.DataSourceAdapter
import com.jade.mvvm.helper.source.helper.DataSourceSnapshotHelper
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ItemKeyedRepository

class BaseItemKeyedDataSource<KEY, MODEL : PageModel<KEY>>(private val itemKeyedRepository: ItemKeyedRepository<KEY, List<MODEL>>) :
    ItemKeyedDataSource<KEY, MODEL>(), DataSourceAdapter<MODEL> {

    private val mDataSourceSnapshotHelper = DataSourceSnapshotHelper<MODEL>(this)
    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()

    override fun loadAfter(params: LoadParams<KEY>, callback: LoadCallback<MODEL>) = load(params, callback, LoadStatus.LOADING_MORE) {
        mDataSourceSnapshotHelper.recordAddLast(it)
    }

    override fun loadBefore(params: LoadParams<KEY>, callback: LoadCallback<MODEL>) = load(
        params, callback, LoadStatus
            .LOADING_BEFORE
    ) {
        mDataSourceSnapshotHelper.recordAddLast(it)
    }

    private fun load(
        params: LoadParams<KEY>, callback: LoadCallback<MODEL>, loadStatus: LoadStatus, snapshotInvoker: (modeList: List<MODEL>)
        -> Unit
    ) {
        if (mDataSourceSnapshotHelper.isOperate()) {
            callback.onResult(mDataSourceSnapshotHelper.getSnapShot())
            return
        }
        mLoadStatusLiveData.postValue(loadStatus)
        itemKeyedRepository.load(params.key, params.requestedLoadSize, object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                snapshotInvoker.invoke(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
                callback.onResult(t)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    override fun loadInitial(params: LoadInitialParams<KEY>, callback: LoadInitialCallback<MODEL>) {
        if (mDataSourceSnapshotHelper.isOperate()) {
            callback.onResult(mDataSourceSnapshotHelper.getSnapShot())
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING_REFRESH)
        itemKeyedRepository.load(params.requestedInitialKey, params.requestedLoadSize, object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                mDataSourceSnapshotHelper.recordUpdate(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
                callback.onResult(t)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    override fun getKey(item: MODEL) = item.getKey()

    override fun getLoadStatusLiveData() = mLoadStatusLiveData

    override fun update(position: Int, model: MODEL) =
        mDataSourceSnapshotHelper.update(position, model)

    override fun remove(position: Int) = mDataSourceSnapshotHelper.remove(position)

    override fun remove(list: List<MODEL>) = mDataSourceSnapshotHelper.remove(list)

    override fun add(position: Int, model: MODEL) = mDataSourceSnapshotHelper.add(position, model)

    override fun add(list: List<MODEL>) = mDataSourceSnapshotHelper.add(list)

    override fun refresh() = mDataSourceSnapshotHelper.refresh()
}