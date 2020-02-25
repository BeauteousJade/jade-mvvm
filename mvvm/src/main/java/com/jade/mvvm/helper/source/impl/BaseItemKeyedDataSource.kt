package com.jade.mvvm.helper.source.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.jade.mvvm.fragment.list.helper.ListOperation
import com.jade.mvvm.helper.source.DataSourceSnapshot
import com.jade.mvvm.helper.source.PageModel
import com.jade.mvvm.helper.source.helper.DataSourceAdapter
import com.jade.mvvm.helper.source.helper.DataSourceSnapshotHelper
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ItemKeyedRepository

open class BaseItemKeyedDataSource<KEY, MODEL : PageModel<KEY>>(
    private val itemKeyedRepository: ItemKeyedRepository<KEY, List<MODEL>>, dataSourceSnapshot:
    DataSourceSnapshot<MODEL>
) :
    ItemKeyedDataSource<KEY, MODEL>(), DataSourceAdapter, ListOperation<MODEL> {

    private val mDataSourceSnapshotHelper = DataSourceSnapshotHelper<MODEL>(this, dataSourceSnapshot)
    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()

    override fun loadAfter(params: LoadParams<KEY>, callback: LoadCallback<MODEL>) =
        load(params, callback, LoadStatus.LOADING_MORE) {
            mDataSourceSnapshotHelper.recordAddLast(it)
        }

    override fun loadBefore(params: LoadParams<KEY>, callback: LoadCallback<MODEL>) = load(
        params, callback, LoadStatus
            .LOADING_BEFORE
    ) {
        mDataSourceSnapshotHelper.recordAddFirst(it)
    }

    private fun load(
        params: LoadParams<KEY>,
        callback: LoadCallback<MODEL>,
        loadStatus: LoadStatus,
        snapshotInvoker: (modeList: List<MODEL>)
        -> Unit
    ) {
        if (mDataSourceSnapshotHelper.isOperate()) {
            snapshotInvoker.invoke(ArrayList(mDataSourceSnapshotHelper.getSnapShot()))
            mDataSourceSnapshotHelper.resetStatus()
            return
        }
        val requestCallback = object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                snapshotInvoker.invoke(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
                callback.onResult(t)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        }
        mLoadStatusLiveData.postValue(loadStatus)
        if (loadStatus == LoadStatus.LOADING_BEFORE) {
            itemKeyedRepository.loadBefore(params.key, params.requestedLoadSize, requestCallback)
        } else if (loadStatus == LoadStatus.LOADING_MORE) {
            itemKeyedRepository.loadAfter(params.key, params.requestedLoadSize, requestCallback)
        }
    }

    override fun loadInitial(params: LoadInitialParams<KEY>, callback: LoadInitialCallback<MODEL>) {
        val callBackInvoker: (list: List<MODEL>) -> Unit = {
            if (params.placeholdersEnabled) {
                callback.onResult(it, 0, getTotalCount())
            } else {
                callback.onResult(it, 0, Int.MAX_VALUE)
            }
        }
        if (mDataSourceSnapshotHelper.isOperate()) {
            callBackInvoker.invoke(ArrayList(mDataSourceSnapshotHelper.getSnapShot()))
            mDataSourceSnapshotHelper.resetStatus()
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING_REFRESH)
        itemKeyedRepository.loadInit(
            params.requestedInitialKey,
            params.requestedLoadSize,
            object : RequestCallback<List<MODEL>> {
                override fun onResult(t: List<MODEL>) {
                    mDataSourceSnapshotHelper.recordUpdate(t)
                    mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
                    callBackInvoker.invoke(t)
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

    open fun getTotalCount() = 0
}