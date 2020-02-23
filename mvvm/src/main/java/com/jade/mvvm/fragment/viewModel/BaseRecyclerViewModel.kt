package com.jade.mvvm.fragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jade.mvvm.fragment.list.helper.ListOperation
import com.jade.mvvm.helper.source.BaseDataSourceFactory
import com.jade.mvvm.helper.source.helper.DataSourceAdapter
import com.jade.mvvm.helper.source.helper.LoadStatus

abstract class BaseRecyclerViewModel<KEY, MODEL>(dataSourceFactory: BaseDataSourceFactory<KEY, MODEL>) :
    ViewModel(),
    ListOperation<MODEL> {

    val mPageListLiveData = LivePagedListBuilder(dataSourceFactory, initConfig()).build()
    private val mDataSourceLiveData = switchMap(dataSourceFactory.mDataSourceLiveData) {
        MutableLiveData(it)
    }
    val mLoadStatusLiveData: LiveData<LoadStatus> = switchMap(mDataSourceLiveData as LiveData<DataSourceAdapter>) {
        it.getLoadStatusLiveData()
    }

    private fun initConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .apply {
                val pageSize = getPageSize()
                val enablePlaceholders = enablePlaceholders()
                val initialLoadSizeHint = getInitialLoadSizeHint()
                val prefetchDistance = getPrefetchDistance()
                setEnablePlaceholders(enablePlaceholders)
                if (pageSize > 0) {
                    setPageSize(pageSize)
                }
                if (initialLoadSizeHint > 0) {
                    setInitialLoadSizeHint(initialLoadSizeHint)
                }
                if (prefetchDistance > 0) {
                    setPrefetchDistance(prefetchDistance)
                }
            }
            .build()
    }

    final override fun refresh() {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.refresh()
        }
    }

    final override fun update(position: Int, model: MODEL) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.update(position, model)
        }
    }

    final override fun remove(position: Int) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.remove(position)
        }
    }

    final override fun remove(list: List<MODEL>) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.remove(list)
        }
    }

    final override fun add(position: Int, model: MODEL) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.add(position, model)
        }
    }

    final override fun add(list: List<MODEL>) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.add(list)
        }
    }

    protected open fun getPageSize() = 20

    protected open fun enablePlaceholders() = false

    protected open fun getInitialLoadSizeHint() = -1

    protected open fun getPrefetchDistance() = -1

    @Suppress("UNCHECKED_CAST")
    private fun asListOperation(dataSource: DataSource<KEY, MODEL>): ListOperation<MODEL>? =
        dataSource as? ListOperation<MODEL>
}