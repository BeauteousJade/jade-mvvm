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

/**
 * 带RecyclerView的Fragment专用ViewModel。
 * 区别于[BaseViewModel]
 */
abstract class BaseRecyclerViewModel<KEY, MODEL>(dataSourceFactory: BaseDataSourceFactory<KEY, MODEL>) :
    ViewModel(),
    ListOperation<MODEL> {

    val mPageListLiveData = LivePagedListBuilder(dataSourceFactory, initConfig()).build()
    private val mDataSourceLiveData = switchMap(dataSourceFactory.mDataSourceLiveData) {
        MutableLiveData(it)
    }
    /**
     * 加载状态的LiveData
     * [com.jade.mvvm.helper.source.helper.LoadStatus]
     */
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

    /**
     * 刷新数据。
     */
    final override fun refresh() {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.refresh()
        }
    }

    /**
     * 更新数据。
     * 将[position]位置上的数据更新为[model]。
     */
    final override fun update(position: Int, model: MODEL) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.update(position, model)
        }
    }

    /**
     * 移除数据。
     * 将[position]位置上的数据从list中移除。
     */
    final override fun remove(position: Int) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.remove(position)
        }
    }

    /**
     * 移除数据。
     * 将[list]从数据源中移除。
     */
    final override fun remove(list: List<MODEL>) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.remove(list)
        }
    }

    /**
     * 添加数据。
     * 向数据源中[position]位置上添加[model]数据。
     */
    final override fun add(position: Int, model: MODEL) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.add(position, model)
        }
    }

    /**
     * 添加数据。
     * 向数据源中添加[list]数据。
     */
    final override fun add(list: List<MODEL>) {
        mDataSourceLiveData.value?.also {
            asListOperation(it)?.add(list)
        }
    }

    /**
     * 返回每页加载的大小
     */
    protected open fun getPageSize() = 20

    /**
     * 数据是否提前占位
     */
    protected open fun enablePlaceholders() = false

    /**
     * 返回第一次请求的Item数量
     */
    protected open fun getInitialLoadSizeHint() = -1

    /**
     * 返回下一页预加载的间距
     */
    protected open fun getPrefetchDistance() = -1

    @Suppress("UNCHECKED_CAST")
    private fun asListOperation(dataSource: DataSource<KEY, MODEL>): ListOperation<MODEL>? =
        dataSource as? ListOperation<MODEL>
}