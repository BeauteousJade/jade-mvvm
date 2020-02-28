package com.jade.mvvm.helper.source.impl

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.jade.mvvm.fragment.list.helper.ListOperation
import com.jade.mvvm.helper.source.DataSourceSnapshot
import com.jade.mvvm.helper.source.helper.DataSourceAdapter
import com.jade.mvvm.helper.source.helper.DataSourceSnapshotHelper
import com.jade.mvvm.helper.source.helper.LoadStatus
import com.jade.mvvm.network.RequestCallback
import com.jade.mvvm.repository.list.ListPositionRepository

open class BasePositionDataSource<MODEL>(
    private val listPositionRepository: ListPositionRepository<List<MODEL>>,
    dataSourceSnapshot: DataSourceSnapshot<MODEL>
) :
    PositionalDataSource<MODEL>(), DataSourceAdapter, ListOperation<MODEL> {

    private val mLoadStatusLiveData = MutableLiveData<LoadStatus>()
    @Suppress("LeakingThis")
    private val mDataSourceSnapshotHelper =
        DataSourceSnapshotHelper<MODEL>(this, dataSourceSnapshot)

    final override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<MODEL>
    ) {
        val callBackInvoker: (list: List<MODEL>) -> Unit = {
            if (params.placeholdersEnabled) {
                callback.onResult(it, 0, getTotalCount())
            } else {
                callback.onResult(it, 0)
            }
        }
        if (mDataSourceSnapshotHelper.isOperate()) {
            callBackInvoker.invoke(ArrayList(mDataSourceSnapshotHelper.getSnapShot()))
            mDataSourceSnapshotHelper.resetStatus()
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING_REFRESH)
        listPositionRepository.loadInit(params.pageSize, object : RequestCallback<List<MODEL>> {
            override fun onResult(t: List<MODEL>) {
                mDataSourceSnapshotHelper.recordUpdate(t)
                callBackInvoker.invoke(t)
                mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
            }

            override fun onError(throwable: Throwable) {
                mLoadStatusLiveData.postValue(LoadStatus.ERROR)
            }
        })
    }

    final override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MODEL>) {
        if (mDataSourceSnapshotHelper.isOperate()) {
            callback.onResult(mDataSourceSnapshotHelper.getSnapShot())
            mDataSourceSnapshotHelper.resetStatus()
            return
        }
        mLoadStatusLiveData.postValue(LoadStatus.LOADING_MORE)
        listPositionRepository.loadMore(
            params.startPosition,
            params.loadSize,
            object : RequestCallback<List<MODEL>> {
                override fun onResult(t: List<MODEL>) {
                    mDataSourceSnapshotHelper.recordAddLast(t)
                    callback.onResult(t)
                    mLoadStatusLiveData.postValue(LoadStatus.SUCCESS)
                }

                override fun onError(throwable: Throwable) {
                    mLoadStatusLiveData.postValue(LoadStatus.ERROR)
                }
            })
    }

    final override fun refresh() = mDataSourceSnapshotHelper.refresh()

    final override fun update(position: Int, model: MODEL) =
        mDataSourceSnapshotHelper.update(position, model)

    final override fun remove(list: List<MODEL>) = mDataSourceSnapshotHelper.remove(list)

    final override fun remove(position: Int) = mDataSourceSnapshotHelper.remove(position)

    final override fun add(list: List<MODEL>) = mDataSourceSnapshotHelper.add(list)

    final override fun add(position: Int, model: MODEL) =
        mDataSourceSnapshotHelper.add(position, model)

    final override fun getLoadStatusLiveData(): MutableLiveData<LoadStatus> = mLoadStatusLiveData

    open fun getTotalCount() = 0
}